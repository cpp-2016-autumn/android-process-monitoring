package com.appmon.control.utils;

import java.util.regex.Pattern;

/**
 * Static helper class for input validation
 * NOTE: may be moved to shared module
 */
public final class Validator {

    // Compiled patterns
    private static Pattern sEmailPattern;
    private static Pattern sPinPattern;

    // email validation pattern
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    // pin validation pattern
    private static final String PIN_PATTERN =
            "^[0-9]{4,}$";
    private static final int MIN_PASSWORD_CHARS = 6;

    // lazily compile pattern
    static {
        sEmailPattern = Pattern.compile(EMAIL_PATTERN);
        sPinPattern = Pattern.compile(PIN_PATTERN);
    }

    // closed constructor
    private Validator() {}

    public static boolean validateEmail(String email) {
        return sEmailPattern.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return password.length() >= MIN_PASSWORD_CHARS;
    }

    public static boolean validatePin(String pin) {
        return sPinPattern.matcher(pin).matches();
    }
}
