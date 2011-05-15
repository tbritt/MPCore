package com.monkeypantssoftware.core.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.webobjects.foundation.NSForwardException;

public class Security {

    public static String hashedString(String string) {
        if (string == null)
            return null;
        
        String hashedString;
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hashedString = Base64.encodeBytes(md.digest(string.getBytes()));
        }
        catch (NoSuchAlgorithmException e) {
            throw new NSForwardException("Your JRE doesn't have SHA-256 for hashing.", e);
        }
        
        return hashedString;
    }
    
    public static boolean isPasswordInsecure(String password) {
        return (password == null) || (password.length() < 6);
    }
    
    public static String guid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
