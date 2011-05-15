package com.monkeypantssoftware.core.component;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * This is a shell. It calls through to NumberFormat.getCurrencyInstance() for 
 * everything. Its only contribution is adding "$" to the source text when parsing
 * but only if it's not the first character.
 */
public class CurrencyFormatterUSD extends NumberFormat {

    private static CurrencyFormatterUSD myCurrencyInstance = new CurrencyFormatterUSD();
    
    public static NumberFormat getCurrencyInstanceUSD() {
        return myCurrencyInstance;
    }
    
    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return NumberFormat.getCurrencyInstance().format(number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return NumberFormat.getCurrencyInstance().format(number, toAppendTo, pos);
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        if (! source.startsWith("$")) {
            source = "$" + source;
        }
        return NumberFormat.getCurrencyInstance().parse(source, parsePosition);
    }

}
