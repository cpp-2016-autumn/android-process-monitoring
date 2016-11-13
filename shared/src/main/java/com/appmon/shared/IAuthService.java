package com.appmon.shared;

import android.support.annotation.Nullable;

/**
 * Auth service interface. Can be used for signing in/out/up,
 * sending password reset email and getting current user.
 */
public interface IAuthService {

    /**
     * Signs in existing user
     *
     * @param email    user e-mail
     * @param password user password
     * @param listener listener, which will handle success and failure of action.
     *                 On success passes {@link IUser} reference to signed in user.
     *                 On failure passes {@link Throwable}. Null value can be used for
     *                 ignoring action result
     */
    void signInWithEmail(String email, String password,
                         @Nullable ResultListener<IUser, Throwable> listener);

    /**
     * Registers new user
     *
     * @param email    new user e-mail
     * @param password user password
     * @param listener listener, which will handle success and failure of action.
     *                 On success passes {@link IUser} reference to signed in user.
     *                 On failure passes {@link Throwable}. Null value can be used for
     *                 ignoring action result
     */
    void registerWithEmail(String email, String password,
                           @Nullable ResultListener<IUser, Throwable> listener);

    /**
     * Sends password reset e-mail to user
     *
     * @param email    e-mail of user, who requests password reset
     * @param listener listener, which will handle success and failure of action.
     *                 On failure passes {@link Throwable}. Null value can be
     *                 used for ignoring action result
     */
    void resetPassword(String email, @Nullable ResultListener<Void, Throwable> listener);

    /**
     * Signs out current user
     */
    void signOut();

    /**
     * Returns current user object as {@link IUser}
     *
     * @return current user object or null, if user is not signed in
     */
    @Nullable
    IUser getUser();
}
