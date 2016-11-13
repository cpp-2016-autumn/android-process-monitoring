package com.appmon.shared.exceptions;

/**
 * Throw when a password is too weak
 */

public class AuthWeakPasswordException extends Throwable {
    public AuthWeakPasswordException(Throwable t) {
        super(t);
    }
}
