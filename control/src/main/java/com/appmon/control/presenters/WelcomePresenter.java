package com.appmon.control.presenters;

import android.support.annotation.NonNull;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.IWelcomeView;

public class WelcomePresenter implements IWelcomePresenter {

    private IUserModel mModel;
    // this presenter can handle only one mView
    private IWelcomeView mView = null;

    public WelcomePresenter(@NonNull IUserModel model) {
        mModel = model;
    }


    @Override
    public void checkUserState() {
        if (mModel.getUserID() != null) {
            mView.startDeviceListActivity();
        }
    }

    @Override
    public void login() {
        mView.startLoginActivity();
    }

    @Override
    public void register() {
        mView.startRegisterActivity();
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
