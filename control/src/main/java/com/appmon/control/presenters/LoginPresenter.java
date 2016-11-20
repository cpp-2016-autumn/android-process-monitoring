package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.ILoginView;

/**
 * Login activity class.
 * IUserModel dependency.
 */
public class LoginPresenter implements ILoginPresenter {

    private IUserModel mModel;
    private ILoginView mView = null;

    // Model messages handlers
    private IUserModel.ISignInListener mSignInListener;
    private IUserModel.IResetPasswordListener mPasswordResetListener;

    public LoginPresenter(IUserModel model) {
        mModel = model;
        // Sign in listener init
        mSignInListener = new IUserModel.ISignInListener() {
            @Override
            public void onSuccess() {
                mView.setProgressVisible(false);
                mView.startDeviceListActivity();
            }

            @Override
            public void onFail(IUserModel.SignInError error) {
                mView.setProgressVisible(false);
                switch (error) {
                    case INVALID_EMAIL:
                        mView.showInputError(ILoginView.InputError.INVALID_EMAIL);
                        break;
                    case WRONG_PASSWORD:
                        mView.showInputError(ILoginView.InputError.WRONG_PASSWORD);
                        break;
                }
            }
        };
        // Password reset listener init
        mPasswordResetListener = new IUserModel.IResetPasswordListener() {
            @Override
            public void onSuccess() {
                mView.showMessage(ILoginView.Message.PASSWORD_RESET_SENT);
            }

            @Override
            public void onFail(IUserModel.ResetPasswordError error) {
                switch (error) {
                    case INVALID_USER:
                        mView.showInputError(ILoginView.InputError.INVALID_EMAIL);
                }
            }
        };
    }

    @Override
    public void resetPassword(String email) {
        mModel.resetPassword(email);
    }

    @Override
    public void signInWithEmail(String email, String password) {
        if (mView != null) {
            mView.clearInputErrors();
            mView.setProgressVisible(true);
        }
        mModel.signInWithEmail(email, password);
    }

    // IBasePresenter

    @Override
    public void attachView(ILoginView view) {
        mView = view;
        mModel.addSignInListener(mSignInListener);
        mModel.addResetPasswordListener(mPasswordResetListener);
    }

    @Override
    public void detachView() {
        mView = null;
        mModel.removeSignInListener(mSignInListener);
        mModel.removeResetPasswordListener(mPasswordResetListener);
    }
}
