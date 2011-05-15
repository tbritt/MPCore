package com.monkeypantssoftware.core.component;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXStatelessComponent;

public class MPBooleanDisplay extends ERXStatelessComponent {
    
    public MPBooleanDisplay(WOContext context) {
        super(context);
    }
    
    public boolean isNull() {
        return valueForBinding("isTrue") == null;
    }

    public Boolean isTrue() {
        return booleanValueForBinding("isTrue", Boolean.TRUE);
    }

    public String trueImage() {
        return stringValueForBinding("trueImage", "true.png");
    }

    public String falseImage() {
        return stringValueForBinding("falseImage", "false.png");
    }

    public String imageHeight() {
        return stringValueForBinding("imageHeight", "20");
    }

}