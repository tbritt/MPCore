package com.monkeypantssoftware.core.utilities;

import com.webobjects.foundation.NSArray;

public enum DateQueryOperator {

    BEFORE ("before", "<"){},
    AFTER ("after", ">"){},
    EQUALS ("equals", "="){};
    
    private String displayName;
    private String value;
    
    DateQueryOperator(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
    }
    
    public String displayName() {
        return displayName;
    }

    public String value() {
        return value;
    }
    
    public static DateQueryOperator dateQueryOperatorForValue(String value) {
        for (DateQueryOperator operator : dateQueryOperators()) {
            if (operator.value().equals(value))
                return operator;
        }
        return null;
    }
    
    public static NSArray<DateQueryOperator> dateQueryOperators() {
        return new NSArray<DateQueryOperator>(DateQueryOperator.values());
    }
    
}
