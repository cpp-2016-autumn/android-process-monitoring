package com.appmon.shared.exceptions;

/**
 * Throw when user entered a wrong password.
 */

public class AuthWrongPasswordException extends Throwable {
    public AuthWrongPasswordException(Throwable t) {
        super(t);
    }
}
