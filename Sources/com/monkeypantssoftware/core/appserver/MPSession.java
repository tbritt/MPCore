package com.monkeypantssoftware.core.appserver;

import java.util.TimeZone;

import org.apache.log4j.Logger;

import er.extensions.appserver.ERXSession;
import er.extensions.foundation.ERXProperties;

public class MPSession extends ERXSession {

    private static final Logger log = Logger.getLogger(MPSession.class.getName());
    private Integer gmtOffsetMinutes;
    private TimeZone timeZone;
    
    public MPSession() {
        super();
        if (ERXProperties.booleanForKey("com.monkeypantssoftware.core.appserver.MPSession.sessionUsesCookies")) {
            setStoresIDsInCookies(true);
            setStoresIDsInURLs(false);
        }
        log.debug("Created session: " + sessionID());
    }
    
    /**
     * Returns the session's time zone based on the offset from setGMTOffsetMinutes. 
     * If no GMT offset has been set, the default time zone is returned.
     */
    public TimeZone timeZone() {
        if (timeZone == null) {
            if (gmtOffsetMinutes == null) {
                timeZone = TimeZone.getDefault();
            }
            else {
                String gmtString = "GMT";
                if (gmtOffsetMinutes.intValue() < 0) {
                    gmtString = gmtString + "+";
                }
                else {
                    gmtString = gmtString + "-";
                }
                gmtString = gmtString + gmtOffsetMinutes / 60;
                timeZone = TimeZone.getTimeZone(gmtString);
            }
        }
        return timeZone;
    }
    
    public void setGMTOffsetMinutes(Integer gmtOffsetMinutes) {
        this.gmtOffsetMinutes = gmtOffsetMinutes;
    }
}
