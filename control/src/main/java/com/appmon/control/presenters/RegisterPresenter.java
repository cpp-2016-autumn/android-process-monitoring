package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.models.user.RegisterError;
import com.appmon.control.views.IRegisterView;

public class RegisterPresenter implements  IRegisterPresenter {

    private  IUserModel mModel;
    private IRegisterView mView = null;

    private IUserModel.IRegisterListener mRegisterListener;

    public RegisterPresenter(IUserModel model) {
        mModel = model;
        mRegisterListener = new IUserModel.IRegisterListener() {
            @Override
            public void onSuccess() {
                mView.showProgress(false);
                mView.startDeviceListActivity();
            }

            @Override
            public void onFail(RegisterError error) {
                mView.showProgress(false);
                switch (error) {
                    case USER_EXISTS:
                        mView.showUserExistsError();
                        break;
                    case INVALID_EMAIL:
                        mView.showInvalidEmailError();
                        break;
                    case WEAK_PASSWORD:
                        mView.showWeakPasswordError();
                        break;
                }
            }
        };
    }

    @Override
    public void attachView(IRegisterView view) {
        mView = view;
        mModel.addRegisterListener(mRegisterListener);
    }

    @Override
    public void detachView() {
        mView = null;
        mModel.removeRegisterListener(mRegisterListener);
    }

    @Override
    public void registerWithEmail(String email, String password) {
        mView.showProgress(true);
        mModel.registerWithEmail(email, password);
    }


}
