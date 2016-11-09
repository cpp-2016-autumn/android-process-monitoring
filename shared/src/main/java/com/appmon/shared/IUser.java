package com.appmon.shared;

import android.support.annotation.NonNull;

/**
 * Represents base user account actions.
 */
public interface IUser {
    enum ChangePasswordError {WEAK_PASSWORD, FRESH_AUTH_NEEDED, INVALID_USER}
    /**
     * @return current user unique ID. Can't be null.
     */
    @NonNull String getUserID();

    /**
     * Changes user password
     * @param password new user password
     */
    void changePassword(String password, ResultListener<Void, ChangePasswordError> listener);

    /**
     * Re-authenticate user, for resolving errors like ChangePasswordError.FRESH_AUTH_NEEDED
     */
    void reauth();
}
