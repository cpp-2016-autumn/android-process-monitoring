package com.appmon.control.presenters;

import android.support.annotation.NonNull;

import com.appmon.control.models.IUserModel;
import com.appmon.control.views.IWelcomeView;

public class WelcomePresenter extends IUserModel.Presenter implements IWelcomePresenter {

    private IUserModel mModel;
    private IWelcomeView mView = null;

    public WelcomePresenter(@NonNull IUserModel model) {
        mModel = model;
        mModel.attachPresenter(this);
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
