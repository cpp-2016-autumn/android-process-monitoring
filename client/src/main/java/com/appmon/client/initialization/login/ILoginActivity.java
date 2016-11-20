package com.appmon.client.initialization.login;

/**
 * An activity that can be used to authenticate users.
 * Has to be controlled by an {@link ILoginController}.
 */
public interface ILoginActivity {
    /**
     * Possible errors.
     */
    enum Error {
        /**
         * User left the email field empty.
         */
        EMAIL_EMPTY,
        /**
         * User entered an invalid email.
         */
        EMAIL_INVALID,
        /**
         * User entered an invalid password.
         */
        PASSWORD_INVALID,
        /**
         * Everything is fine.
         */
        NO_ERROR
    }

    /**
     * Sets an error state on this activity.
     * @param error One of the possible {@link Error} values.
     */
    void setError(Error error);

    /**
     * Called on a successful login attempt.
     */
    void loginSuccessful();

    /**
     * Hides or shows a progress bar.
     * @param show When true, shows the progress bar.
     */
    void showProgress(boolean show);
}
