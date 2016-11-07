package com.appmon.client.initialization.login;

/**
 * Interface for {@link LoginController}
 * Created by MikeSotnichek on 11/7/2016.
 */

public interface ILoginController {
    void hookActivity(ILoginActivity activity);

    void attemptLogin(String email, String password);
}
