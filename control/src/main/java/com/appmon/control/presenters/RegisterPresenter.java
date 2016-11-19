package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.IRegisterView;

/**
 * Register presenter class.
 * IUserMode Dependency.
 */
public class RegisterPresenter implements IRegisterPresenter {

    private IUserModel mModel;
    private IRegisterView mView = null;

    private IUserModel.IRegisterListener mRegisterListener;

    public RegisterPresenter(IUserModel model) {
        mModel = model;
        // init register listener
        mRegisterListener = new IUserModel.IRegisterListener() {
            @Override
            public void onSuccess() {
                mView.setProgressVisible(false);
                mView.startDeviceListActivity();
            }

            @Override
            public void onFail(IUserModel.RegisterError error) {
                mView.setProgressVisible(false);
                switch (error) {
                    case USER_EXISTS:
                        mView.showInputError(IRegisterView.InputError.USER_EXISTS);
                        break;
                    case INVALID_EMAIL:
                        mView.showInputError(IRegisterView.InputError.INVALID_EMAIL);
                        break;
                    case WEAK_PASSWORD:
                        mView.showInputError(IRegisterView.InputError.WEAK_PASSWORD);
                        break;
                }
            }
        };
    }

    @Override
    public void registerWithEmail(String email, String password) {
        if (mView != null) {
            mView.clearInputErrors();
            mView.setProgressVisible(true);
        }
        mModel.registerWithEmail(email, password);
    }

    // IBasePresenter

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


}
