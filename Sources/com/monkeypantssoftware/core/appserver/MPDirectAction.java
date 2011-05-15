package com.monkeypantssoftware.core.appserver;

import java.text.SimpleDateFormat;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation._NSUtilities;

import er.extensions.appserver.ERXDirectAction;

public class MPDirectAction extends ERXDirectAction {
    
    @SuppressWarnings({ "rawtypes" })
    private static final Class[] DIRECT_ACTION_PARAMETERS = { WORequest.class };
    
    public static final NSNumberFormatter NUMBER_FORMATTER = new NSNumberFormatter();
    public static final SimpleDateFormat ISO_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat LONG_DATE_FORMATTER = new SimpleDateFormat("MMMM d, yyyy");
    
    public MPDirectAction(WORequest r) {
        super(r);
    }
    
    /**
     * TODO HACK.  Using a bunch of WebObjects internals.  I'm disgusted.
     */
    @SuppressWarnings("rawtypes")
    public static WOActionResults performActionNamed(String actionClass, String actionName, WOContext aContext) {
        Class  daClass = _NSUtilities.classWithName(actionClass);
        WODirectAction daInstance = (WODirectAction) _NSUtilities.instantiateObject(daClass, DIRECT_ACTION_PARAMETERS, new Object[]{ aContext.request() }, true, WOApplication.application().isDebuggingEnabled());
        WOActionResults results = daInstance.performActionNamed(actionName);
        
        return results;
    }
}
