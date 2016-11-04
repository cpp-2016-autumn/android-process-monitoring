package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.models.user.SignInError;
import com.appmon.control.views.ILoginView;

public class LoginPresenter implements ILoginPresenter {

    private IUserModel mModel;
    private ILoginView mView = null;

    private IUserModel.ISignInListener mSignInListener;

    public LoginPresenter(IUserModel model) {
        mModel = model;
        mSignInListener = new IUserModel.ISignInListener() {
            @Override
            public void onSuccess() {
                mView.showProgress(false);
                mView.startDeviceListActivity();
            }

            @Override
            public void onFail(SignInError error) {
                mView.showProgress(false);
                switch (error) {
                    case INVALID_USER:
                        mView.showError(ILoginView.Error.INVALID_USER);
                        break;
                    case WRONG_PASSWORD:
                        mView.showError(ILoginView.Error.WRONG_PASSWORD);
                        break;
                }
            }
        };
    }

    @Override
    public void attachView(ILoginView view) {
        mView = view;
        mModel.addSignInListener(mSignInListener);
    }

    @Override
    public void detachView() {
        mModel.removeSignInListener(mSignInListener);
        mView = null;
    }

    @Override
    public void signInWithEmail(String email, String password) {
        mView.clearErrors();
        mView.showProgress(true);
        mModel.signInWithEmail(email, password);
    }
}
