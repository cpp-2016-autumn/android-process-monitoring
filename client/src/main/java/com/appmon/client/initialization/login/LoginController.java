package com.appmon.client.initialization.login;

import com.appmon.shared.exceptions.AuthInvalidEmailException;
import com.appmon.shared.exceptions.AuthWrongPasswordException;
import com.appmon.shared.firebase.FirebaseCloudServices;
import com.appmon.shared.IAuthService;
import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
import com.appmon.shared.utils.Validator;

/**
 * Controls {@code LoginActivity}.
 */
public class LoginController implements ILoginController {
    private static LoginController Instance = null;
    private IAuthService mAuthService;

    public static LoginController getInstance() {
        if (Instance == null) Instance =
                new LoginController(FirebaseCloudServices.getInstance().getAuth());
        return Instance;
    }

    private ILoginActivity mLoginActivity;

    public LoginController(IAuthService authService) {
        mAuthService = authService;
    }

    @Override
    public void hookActivity(ILoginActivity activity) {
        mLoginActivity = activity;
    }

    @Override
    public void attemptLogin(String email, String password) {
        mLoginActivity.showProgress(true);
        ILoginActivity.Error error = null;

        // Check for a valid email address/password.
        if (email.isEmpty()) {
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
            mAuthService.signInWithEmail(email, password, new ResultListener<IUser, Throwable>() {
                @Override
                public void onSuccess(IUser user) {
                    mLoginActivity.setError(ILoginActivity.Error.NO_ERROR);
                    mLoginActivity.showProgress(false);
                    mLoginActivity.loginSuccessful();
                }

                @Override
                public void onFailure(Throwable err) {
                    mLoginActivity.showProgress(false);
                    if (err instanceof AuthWrongPasswordException) {
                        mLoginActivity.setError(ILoginActivity.Error.PASSWORD_INVALID);
                    } else if (err instanceof AuthInvalidEmailException) {
                        mLoginActivity.setError(ILoginActivity.Error.EMAIL_INVALID);
                    } else {
                        mLoginActivity.setError(ILoginActivity.Error.UNKNOWN_ERROR);
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
