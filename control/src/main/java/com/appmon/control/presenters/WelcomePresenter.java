package com.appmon.control.presenters;

import android.support.annotation.NonNull;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.IWelcomeView;

/**
 * Welcome presenter class
 */
public class WelcomePresenter implements IWelcomePresenter {

    private IUserModel mModel;
    // this presenter can handle only one mView
    private IWelcomeView mView = null;

    public WelcomePresenter(@NonNull IUserModel model) {
        mModel = model;
    }

    @Override
    public void postPin(String pin) {
        if (mModel.getUserID() != null) {
            String realPin = mModel.getAppPin();
            if (mView != null) {
                if (realPin == null || realPin.equals(pin)) {
                    mView.startDeviceListActivity();
                }
            }
        }
    }

    @Override
    public void logOut() {
        mModel.signOut();
        mView.setPinFormVisibility(false);
    }

    @Override
    public void checkUserState() {
        if (mModel.getUserID() != null) {
            if (mView != null) {
                if (mModel.getAppPin() == null) {
                    mView.startDeviceListActivity();
                } else {
                    mView.setPinFormVisibility(true);
                }
            }
        } else {
            mView.setPinFormVisibility(false);
        }
    }

    @Override
    public void login() {
        if (mView != null) {
            mView.startLoginActivity();
        }
    }

    @Override
    public void register() {
        if (mView != null) {
            mView.startRegisterActivity();
        }
    }

    @Override
    public void attachView(IWelcomeView view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
