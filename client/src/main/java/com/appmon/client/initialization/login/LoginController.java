package com.appmon.client.initialization.login;

import android.text.TextUtils;

import com.appmon.shared.firebase.FirebaseCloudServices;
import com.appmon.shared.IAuthService;
import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
import com.appmon.shared.utils.Validator;

/**
 * Controls LoginActivity
 * Created by MikeSotnichek on 11/7/2016.
 */
public class LoginController implements ILoginController {
    private static LoginController Instance = new LoginController();
    private IAuthService authService;

    public static LoginController getInstance() {
        return Instance;
    }

    private ILoginActivity mLoginActivity;

    private LoginController() {
        authService = FirebaseCloudServices.getInstance().getAuth();
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

        // Check for a valid email address/password.
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
            authService.signInWithEmail(email, password, new ResultListener<IUser, IAuthService.SignInError>() {
                @Override
                public void onSuccess(IUser user){
                    mLoginActivity.loginSuccessful(user.getUserID());
                }
                @Override
                public void onFailure(IAuthService.SignInError err){
                    switch (err){
                        case WRONG_PASSWORD:
                            mLoginActivity.setError(ILoginActivity.Error.PASSWORD_INVALID);
                            break;
                        case INVALID_EMAIL:
                            mLoginActivity.setError(ILoginActivity.Error.EMAIL_INVALID);
                            break;
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return Validator.validateEmail(email);
    }

    private boolean isPasswordValid(String password) {
        return Validator.validatePassword(password);
    }

}
