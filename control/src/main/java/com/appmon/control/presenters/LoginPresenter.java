package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.ILoginView;

public class LoginPresenter implements ILoginPresenter {

    private IUserModel mModel;
    private ILoginView mView = null;

    private IUserModel.ISignInListener mSignInListener;
    private IUserModel.IPasswordResetListener mPasswordResetListener;

    public LoginPresenter(IUserModel model) {
        mModel = model;

        mSignInListener = new IUserModel.ISignInListener() {
            @Override
            public void onSuccess() {
                mView.showProgress(false);
                mView.startDeviceListActivity();
            }

            @Override
            public void onFail(IUserModel.SignInError error) {
                mView.showProgress(false);
                switch (error) {
                    case INVALID_USER:
                        mView.showInputError(ILoginView.InputError.INVALID_USER);
                        break;
                    case WRONG_PASSWORD:
                        mView.showInputError(ILoginView.InputError.WRONG_PASSWORD);
                        break;
                }
            }
        };

        mPasswordResetListener = new IUserModel.IPasswordResetListener() {
            @Override
            public void onSuccess() {
                mView.showMessage(ILoginView.Message.PASSWORD_RESET_SENT);
            }

            @Override
            public void onFail(IUserModel.PasswordResetError error) {
                switch (error) {
                    case INVALID_USER:
                        mView.showInputError(ILoginView.InputError.INVALID_USER);
                }
            }
        };
    }

    @Override
    public void attachView(ILoginView view) {
        mView = view;
        mModel.addSignInListener(mSignInListener);
        mModel.addPasswordResetListener(mPasswordResetListener);
    }

    @Override
    public void detachView() {
        mView = null;
        mModel.removeSignInListener(mSignInListener);
        mModel.removePasswordResetListener(mPasswordResetListener);
    }

    @Override
    public void resetPassword() {
        mModel.resetPassword();
    }

    @Override
    public void signInWithEmail(String email, String password) {
        mView.clearInputErrors();
        mView.showProgress(true);
        mModel.signInWithEmail(email, password);
    }
}
