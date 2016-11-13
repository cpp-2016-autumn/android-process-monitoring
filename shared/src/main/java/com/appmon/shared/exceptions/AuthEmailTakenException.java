package com.appmon.shared.exceptions;

/**
 * Throw when a user with specified email already exists.
 */

public class AuthEmailTakenException extends Throwable {
    public AuthEmailTakenException(Throwable t) {
        super(t);
    }
}
