package com.appmon.control.presenters;

import android.util.Log;

import com.appmon.control.models.IUserModel;
import com.appmon.control.models.SignInError;
import com.appmon.control.views.ILoginView;

public class LoginPresenter extends IUserModel.Presenter implements ILoginPresenter {

    private IUserModel mModel;
    private ILoginView mView = null;

    public LoginPresenter(IUserModel model) {
        mModel = model;
        mModel.attachPresenter(this);
    }

    @Override
    public void attachView(ILoginView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onAuthSuccess() {
        mView.hideProgress();
        mView.startDeviceListActivity();
    }

    @Override
    public void onSignInError(SignInError error) {
        mView.hideProgress();
        switch (error) {
            case INVALID_USER:
                mView.showInvalidUserError();
                break;
            case WRONG_PASSWORD:
                mView.showWrongPasswordError();
                break;
        }
    }

    @Override
    public void signInWithEmail(String email, String password) {
        mView.showProgress();
        mModel.signInWithEmail(email, password);
    }
}
