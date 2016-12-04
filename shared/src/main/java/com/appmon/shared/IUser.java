package com.appmon.shared;

import android.support.annotation.NonNull;

/**
 * Represents base user account actions.
 */
public interface IUser {
    enum ChangePasswordError {WEAK_PASSWORD, REAUTH_NEEDED, INTERNAL_ERROR}
    /**
     * @return current user unique ID. Can't be null.
     */
    @NonNull String getUserID();

    /**
     * Changes user password
     * @param password new user password
     */
    void changePassword(String password, ResultListener<Void, ChangePasswordError> listener);
}
