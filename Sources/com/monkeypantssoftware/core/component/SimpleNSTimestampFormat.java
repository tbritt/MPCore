package com.monkeypantssoftware.core.component;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.webobjects.foundation.NSTimestamp;

public class SimpleNSTimestampFormat extends SimpleDateFormat {

    @Override
    public Date parse(String text, ParsePosition pos) {
        return new NSTimestamp(super.parse(text, pos));
    }
    
}
