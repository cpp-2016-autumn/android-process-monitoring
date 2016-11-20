package com.appmon.client.initialization.login;

/**
 * Interface for {@link LoginActivity}
 * Created by MikeSotnichek on 11/7/2016.
 */

public interface ILoginActivity {
    enum Error {
        EMAIL_EMPTY,
        EMAIL_INVALID,
        PASSWORD_INVALID,
        NO_ERROR
    }

    void setError(Error error);

    void loginSuccessful();

    void showProgress(boolean show);
}
