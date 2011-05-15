package com.monkeypantssoftware.core.component;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;

import er.extensions.components.ERXComponent;

public class MPExceptionPage extends ERXComponent {
    
    private Throwable exception;
    
    public MPExceptionPage(WOContext context) {
        super(context);
    }
    
    public Throwable exception() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
    
    @Override
    public void appendToResponse(WOResponse response, WOContext context) {
        super.appendToResponse(response, context);
        session().terminate();
    }
}