package com.appmon.shared;

import android.support.annotation.Nullable;

/**
 * Auth service interface. Can be used for signing in/out/up,
 * sending password reset email and getting current user.
 */
public interface IAuthService {
    // Error types, which can be passed to listeners by implementation
    enum SignInError {INVALID_EMAIL, WRONG_PASSWORD}
    enum RegisterError {INVALID_EMAIL, WEAK_PASSWORD, USER_EXISTS}
    enum ResetPasswordError{INVALID_EMAIL}

    /**
     * Signs in existing user
     * @param email user e-mail
     * @param password user password
     * @param listener listener, which will handle success and failure of action.
     *                 On success passes {@link IUser} reference to signed in user.
     *                 On failure passes {@link SignInError}. Null value can be used for
     *                 ignoring action result
     */
    void signInWithEmail(String email, String password,
                         @Nullable ResultListener<IUser, SignInError> listener);

    /**
     * Registers new user
     * @param email new user e-mail
     * @param password user password
     * @param listener listener, which will handle success and failure of action.
     *                 On success passes {@link IUser} reference to signed in user.
     *                 On failure passes {@link RegisterError}. Null value can be used for
     *                 ignoring action result
     */
    void registerWithEmail(String email, String password,
                           @Nullable ResultListener<IUser, RegisterError> listener);

    /**
     * Sends password reset e-mail to user
     * @param email e-mail of user, who requests password reset
     * @param listener listener, which will handle success and failure of action.
     *                 On failure passes {@link ResetPasswordError}. Null value can be
     *                 used for ignoring action result
     */
    void resetPassword(String email, @Nullable ResultListener<Void, ResetPasswordError> listener);

    /**
     * Signs out current user
     */
    void signOut();

    /**
     * Returns current user object as {@link IUser}
     * @return current user object or null, if user is not signed in
     */
    @Nullable IUser getUser();
}
