package com.appmon.client.initialization.login;

/**
 * A controller for a login activity.
 * Controls an {@link ILoginActivity}.
 */
public interface ILoginController {
    /**
     * Hooks an activity to this controller.
     *
     * @param activity An activity that will be hooked to this controller.
     */
    void hookActivity(ILoginActivity activity);

    /**
     * Attempts to login using the provided credentials.
     *
     * @param email    An email to try to login with.
     * @param password A password to try to login with.
     */
    void attemptLogin(String email, String password);
}
