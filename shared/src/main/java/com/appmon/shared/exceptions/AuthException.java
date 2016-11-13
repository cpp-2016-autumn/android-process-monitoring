package com.appmon.shared.exceptions;

/**
 * Throw when an unknown auth error occurred.
 */

public class AuthException extends Throwable {

    public AuthException(Throwable t){
        super(t);
    }
}
