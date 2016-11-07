package com.appmon.client.initialization.login;

import android.text.TextUtils;

/**
 * Controls LoginActivity
 * Created by MikeSotnichek on 11/7/2016.
 */
public class LoginController implements ILoginController {
    private static LoginController Instance = new LoginController();

    public static LoginController getInstance() {
        return Instance;
    }

    private ILoginActivity mLoginActivity;

    private LoginController() {
        //TODO: Probably Firebase init
    }

    @Override
    public void hookActivity(ILoginActivity activity) {
        mLoginActivity = activity;
    }

    @Override
    public void attemptLogin(String email, String password) {
        // Reset errors.
        mLoginActivity.setError(ILoginActivity.Error.NO_ERROR);
        ILoginActivity.Error error = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            error = ILoginActivity.Error.EMAIL_EMPTY;
        } else if (!isEmailValid(email)) {
            error = ILoginActivity.Error.EMAIL_INVALID;
        }
        if (!isPasswordValid(password)) {
            error = ILoginActivity.Error.PASSWORD_INVALID;
        }
        if (error != null) {
            mLoginActivity.setError(error);
        } else {
            //TODO: Actual login
        }
        mLoginActivity.loginSuccessful();
    }

    private boolean isEmailValid(String email) {
        //TODO: Use a shared validator
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Use a shared validator
        return password.length() > 4;
    }

}
