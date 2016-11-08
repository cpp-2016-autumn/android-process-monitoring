package com.appmon.shared;

/**
 * Wraps possible database errors
 */
public enum DatabaseError {
    /// on Firebase	NETWORK_ERROR or DISCONNECTED or MAX_RETRIES(?)
    NETWORK_ERROR,
    /// on Firebase PERMISSION_DENIED
    ACCESS_DENIED,
    //// on Firebase EXPIRED_TOKEN
    FRESH_AUTH_NEEDED,
    /// all other errors
    INTERNAL_FAIL,
}
