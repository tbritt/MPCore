package com.monkeypantssoftware.core.appserver;

import org.apache.log4j.Logger;

import com.ibm.icu.util.TimeZone;
import com.monkeypantssoftware.core.component.MPExceptionPage;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WORequestHandler;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOSharedEditingContext;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimeZone;

import er.ajax.AjaxUtils;
import er.extensions.appserver.ERXApplication;

public class MPApplication extends ERXApplication {
    
    protected static Logger log = Logger.getLogger(MPApplication.class);
    
    public MPApplication() {
        super();
        
        setAllowsConcurrentRequestHandling(true);
        
        WORequestHandler directActionRequestHandler = requestHandlerForKey(directActionRequestHandlerKey());
        setDefaultRequestHandler(directActionRequestHandler);
        
        EOSharedEditingContext.setDefaultSharedEditingContext(null);
        
        NSTimeZone.setDefaultTimeZone(NSTimeZone.getGMT());
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        
        // Work-around for WO 5.4.3 bug regarding WSR.
        frameworksBaseURL();
        if (System.getProperty("WOFrameworksBaseURL") != null) {
            setFrameworksBaseURL(System.getProperty("WOFrameworksBaseURL"));
        }
    }
    
    /**
     * If someone is asking the WO app for a favicon.ico we just return an empty response
     * rather than creating a bogus session and/or throwing an exception.
     * 
     * @author tbritt@phigment.org
     */
    @Override
    public WOResponse dispatchRequest(WORequest request) {
        if (request.uri().endsWith("favicon.ico")) {
            return new WOResponse();
        }
        return super.dispatchRequest(request);
    }
    
    /**
     * All uncaught exceptions that occur within the request/response loop will 
     * float up and be handled by this method.
     * 
     * @author tbritt@phigment.org
     */
    @Override
    public WOResponse handleException(Exception exception, WOContext aContext ) {
        log.error("*** UNCAUGHT EXCEPTION ***", exception);

        MPExceptionPage page = pageWithName(MPExceptionPage.class);
        page.setException(exception);

        if (AjaxUtils.isAjaxRequest(aContext.request())) {
            // Break out of AJAX.
            AjaxUtils.redirectTo(page);
            WOResponse redirect = page.context().response();
            aContext.session().savePage(page);
            return redirect;
        }
        
        return page.generateResponse();
    }
    
    /**
     * If a user's session has timed out or otherwise can't be restored, this 
     * method will be called. You should either override this method or implement 
     * a "login" direct action.
     * 
     * @author tbritt@phigment.org
     */
    @Override
    public WOResponse handleSessionRestorationErrorInContext(WOContext context) {
        NSMutableDictionary<String, Object> noWOSID = new NSMutableDictionary<String, Object>();
        noWOSID.setObjectForKey(Boolean.FALSE, "wosid");
        noWOSID.setObjectForKey(Boolean.TRUE, "sessionTimeout");
        
        String url = context.directActionURLForActionNamed("login", noWOSID);
        WORedirect redirect = new WORedirect(context);
        redirect.setUrl(url);
        
        if (AjaxUtils.isAjaxRequest(context.request())) {
            // Break out of AJAX.
            AjaxUtils.redirectTo(redirect);
            return redirect.context().response();
        }
        
        return redirect.generateResponse();
    }
    
    public String version() {
        return System.getProperty("VersionNumber");
    }
    
    public String revision() {
        return System.getProperty("RevisionNumber");
    }
    
    public String buildNumber() {
        return System.getProperty("BuildNumber");
    }
}
