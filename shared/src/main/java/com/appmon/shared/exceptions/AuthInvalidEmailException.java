package com.appmon.shared.exceptions;

/**
 * Throw when the email provided is invalid or user with such email is not registered.
 */

public class AuthInvalidEmailException extends Throwable {
    public AuthInvalidEmailException(Throwable t) {
        super(t);
    }
}
