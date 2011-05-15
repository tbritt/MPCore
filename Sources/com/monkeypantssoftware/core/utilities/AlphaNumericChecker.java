package com.monkeypantssoftware.core.utilities;

public class AlphaNumericChecker {
	
	private static String ALPHA_NUMERIC_CHECK = "[a-zA-Z0-9-]*"; //Checks for Alpha Numeric + dashes


	 public static boolean validateStringAsAlphaNumericPlusDashes(String s) {	        
	        return ( s != null && s.matches(ALPHA_NUMERIC_CHECK) && s.length() > 0 );
	    }
	
}
