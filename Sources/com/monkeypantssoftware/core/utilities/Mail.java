package com.monkeypantssoftware.core.utilities;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Mail {

    public static String systemFromAddress() {
        String  systemFromAddress = System.getProperty("SystemFromAddress");
        if (systemFromAddress == null)
            throw new IllegalStateException("SystemFromAddress property is not set!");
        
        return systemFromAddress;
    }
    
    public static String systemReplyToAddress() {
        String  systemReplyToAddress = System.getProperty("SystemReplyToAddress");
        if (systemReplyToAddress == null)
            throw new IllegalStateException("SystemReplyToAddress property is not set!");
        
        return systemReplyToAddress;
    }
    
    /**
     * Validate the form of an email address.
     * 
     * <P>
     * Return <tt>true</tt> only if
     * <ul>
     * <li> <tt>address</tt> can successfully construct an
     * {@link javax.mail.internet.InternetAddress}
     * <li> when parsed with "@" as delimiter, <tt>address</tt> contains
     * two tokens which are not zero length.
     * </ul>
     * 
     * <P>
     * The second condition arises since local email addresses, simply of the
     * form "<tt>bob</tt>", for example, are valid for
     * {@link javax.mail.internet.InternetAddress}, but almost always
     * undesired.
     */
    public static boolean isValidEmailAddress(String address) {
        if (address == null) return false;
        boolean result = true;
        try {
            new InternetAddress(address);
            
            if ( ! hasNameAndDomain(address))
                result = false;
            
            if (address.indexOf(" ") > -1) // Do not allow spaces
                result = false;
            
        } catch (AddressException ex) {
            result = false;
        }
        
        return result;        
    }

    private static boolean hasNameAndDomain(String aEmailAddress){
        String[] tokens = aEmailAddress.split("@");
        return 
            tokens.length == 2 && 
            tokens[0].length() > 0 &&
            tokens[1].length() > 0 &&
            tokens[1].indexOf(".") > -1 &&
            ! tokens[1].endsWith(".") && 
            ! tokens[1].startsWith(".");
    }
}
