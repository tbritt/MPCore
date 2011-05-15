package com.monkeypantssoftware.core.component;

import java.text.NumberFormat;

import com.monkeypantssoftware.core.appserver.MPApplication;
import com.monkeypantssoftware.core.appserver.MPSession;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOAdaptorChannel;
import com.webobjects.eoaccess.EOAdaptorOperation;
import com.webobjects.eoaccess.EODatabaseContext;
import com.webobjects.eoaccess.EODatabaseOperation;
import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxUtils;
import er.extensions.appserver.ERXWOContext;
import er.extensions.components.ERXComponent;
import er.extensions.eof.ERXEC;
import er.extensions.formatters.ERXUnitAwareDecimalFormat;
import er.extensions.foundation.ERXValueUtilities;

public class MPComponent extends ERXComponent {

    private SimpleNSTimestampFormat timestampFormatterWithUsersTimeZone;
    private String uniqueIDForThisInstance;
    private EOEditingContext editingContext;
    
    protected NSMutableDictionary<String, Object> invalidData = new NSMutableDictionary<String, Object>();
    protected NSMutableDictionary<String, String> validationErrMsgs = new NSMutableDictionary<String, String>();
    
    public int rowNumber;
    public String validationErrorMsg;
    
    public MPComponent(WOContext context) {
        super(context);
    }
    
    public boolean isAjaxRequest() {
        return AjaxUtils.isAjaxRequest(context().request());
    }
    
    public EOEditingContext editingContext() {
        if (editingContext == null) {
            editingContext = ERXEC.newEditingContext();
        }
        return editingContext;
    }
    
    /**
     * Useful for appending to ERXJSOpenWindowHyperlink's fragment attribute 
     * to invalidate browser cache.
     * 
     * @author tbritt@phigment.org
     */
    public String timestampString() {
        return Long.valueOf(new NSTimestamp().getTime()).toString();
    }
    
    /**
     * Returns a SimpleDateFormatter set to the Session's timeZone() value.
     *
     * @author tbritt@phigment.org
     */
    public SimpleNSTimestampFormat timestampFormatterWithUsersTimeZone() {
        if (timestampFormatterWithUsersTimeZone == null) {
            timestampFormatterWithUsersTimeZone = new SimpleNSTimestampFormat();
            timestampFormatterWithUsersTimeZone.setTimeZone(session().timeZone());
            timestampFormatterWithUsersTimeZone.applyPattern("MM/dd/yyyy");
        }
        return timestampFormatterWithUsersTimeZone;
    }
    
    @Override
    public boolean booleanValueForBinding(String bindingName) {
        return ERXValueUtilities.booleanValueWithDefault(valueForBinding(bindingName), true);
    }
    
    @Override
    public void validationFailedWithException(Throwable exception, Object value, String keyPath) {
        super.validationFailedWithException(exception, value, keyPath);
        validationErrMsgs.takeValueForKey(exception.getMessage(), keyPath);
        if (value == null)
            value = NSKeyValueCoding.NullValue;
        invalidData.takeValueForKey(value, keyPath);
    }
    
    @Override
    public Object valueForKeyPath(String keyPath) {
        if (invalidData.containsKey(keyPath)) {
            Object value = invalidData.objectForKey(keyPath);
            if (value == NSKeyValueCoding.NullValue)
                value = null;
            return value;
        }
        return super.valueForKeyPath(keyPath);
    }
    
    @Override
    public void awake() {
        invalidData.removeAllObjects();
        validationErrMsgs.removeAllObjects();
        super.awake();
    }
    
    public NSArray<String> validationErrorMessageStrings() {
        return validationErrMsgs.allValues();
    }
    
    public NSArray<String> validationErrorMessageKeyPaths() {
        return validationErrMsgs.allKeys();
    }
    
    public String validationErrorMessageForKeyPath(String keyPath) {
        Object errMsg = validationErrMsgs.valueForKey(keyPath);
        if (errMsg == null)
            return "";
        return errMsg.toString();
    }
    
    public boolean validationFailed() {
        return (validationErrMsgs.count() > 0);
    }
    
    @SuppressWarnings("rawtypes")
    protected void handleOptimisticLockingFailureByRefaulting(EOGeneralAdaptorException lockingException) {
        NSDictionary info = lockingException.userInfo();
        
        // Determine the adaptor operation that triggered the optimistic locking failure.
        EOAdaptorOperation adaptorOperation = (EOAdaptorOperation)info.objectForKey(
                EOAdaptorChannel.FailedAdaptorOperationKey);
        int operationType = adaptorOperation.adaptorOperator();
        
        // Determine the database operation that triggered the failure.
        EODatabaseOperation dbOperation = (EODatabaseOperation)info.objectForKey(
                EODatabaseContext.FailedDatabaseOperationKey);
        
        // Retrieve the enterprise object that triggered the failure.
        EOEnterpriseObject failedEO = (EOEnterpriseObject)dbOperation.object();
        
        // Retrieve the dictionary of values involved in the failure and take action
        // based on the type of adaptor operation that triggered the optimistic 
        // locking failure.
        if (operationType == EODatabaseOperation.AdaptorUpdateOperator) {
            
            // Recover by refaulting the enterprise object involved in the failure.
            // This refreshes the EO's data and allows the user to try again.
            EOEditingContext ec = failedEO.editingContext();
            ec.refaultObject(failedEO);
        }
        else { 
            
            // The optimistic locking failure was caused by another type of adaptor 
            // operation, not an update.
            throw new NSForwardException(lockingException, "Unknown adaptorOperator " + 
                    operationType + " in optimistic locking exception.");
        }
    }
    
    /**
     * Determines if the exception is due to an optimistic locking failure.
     * 
     * @param  exceptionWhileSaving
     * @return true if the exception was caused by optimistic locking.
     */
    @SuppressWarnings("rawtypes")
    protected boolean isOptimisticLockingFailure(EOGeneralAdaptorException exceptionWhileSaving) {
        NSDictionary exceptionInfo = exceptionWhileSaving.userInfo();
        Object failureType = (exceptionInfo != null) ? 
                exceptionInfo.objectForKey(EOAdaptorChannel.AdaptorFailureKey) : null;
        return ((failureType != null) && 
                (failureType.equals(EOAdaptorChannel.AdaptorOptimisticLockingFailure)));
    }
    
    /**
     * Useful for reusable components that need to refer to an ajax update id.
     * 
     * @author tbritt@phigment.org
     */
    public String uniqueIDForThisInstance() {
        if (uniqueIDForThisInstance == null) 
            uniqueIDForThisInstance = ERXWOContext.safeIdentifierName(context(), true);
        return uniqueIDForThisInstance;
    }
    
    
    /**
     * Useful for formatting file sizes. For example if you have a Number
     * representing bytes this will output a human-friendly string like
     * 12.5 KB, or 2.4 MB. Bind to a WOString's formatter.
     * 
     * @author tbritt@phigment.org
     */
    public NumberFormat fileSizeFormatter() {
        NumberFormat formatter = new ERXUnitAwareDecimalFormat(ERXUnitAwareDecimalFormat.BYTE);
        formatter.setMaximumFractionDigits(1);
        return formatter;
    }
    
    /**
     * Used for alternating colors in table rows.
     * 
     * @param  Uses rowNumber, which should be set in the WORepetition.
     * @return The string "alternate" for every other row, which should be defined in the style sheet.
     * @author tbritt@phigment.org
     */
    public String rowClass() {
        return (rowNumber % 2 == 0) ? "" : "alternate";
    }
    
    /**
     * Convenience method that returns a properly typed session object.
     * 
     * @author tbritt@phigment.org
     */
    @Override
    public MPSession session() {
        return (MPSession)super.session();
    }
    
    /**
     * Convenience method that returns a properly typed application object.
     * 
     * @author tbritt@phigment.org
     */
    @Override
    public MPApplication application() {
        return (MPApplication)super.application();
    }
    
    public NumberFormat currencyFormatter() {
        return CurrencyFormatterUSD.getCurrencyInstanceUSD();
    }

}
