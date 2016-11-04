package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.models.user.SignOutError;
import com.appmon.control.views.ISettingsView;

public class SettingsPresenter implements ISettingsPresenter {

    private IUserModel mModel;
    private ISettingsView mView = null;

    private IUserModel.ISignOutListener mSignOutListener = null;

    public SettingsPresenter(IUserModel model) {
        mModel = model;

        mSignOutListener = new IUserModel.ISignOutListener() {
            @Override
            public void onSuccess() {
                mView.startWelcomeActivity();
            }

            @Override
            public void onFail(SignOutError error) {
                // TODO: signOut Error ?
            }
        };
    }

    @Override
    public void attachView(ISettingsView view) {
        mView = view;
        mModel.addSignOutListener(mSignOutListener);
    }

    @Override
    public void detachView() {
        mView = null;
        mModel.removeSignOutListener(mSignOutListener);
    }

    @Override
    public void changePassword(String password, String passwordRepeat) {
        if (!password.equals(passwordRepeat)) {
            mView.showError(ISettingsView.Error.DIFFERENT_PASSWORDS);
            return;
        }
        mModel.changePassword(password);
    }

    @Override
    public void signOut() {
        mModel.signOut();
    }

    @Override
    public void changeAppPin(String pin, String pinRepeat) {
        if (!pin.equals(pinRepeat)) {
            mView.showError(ISettingsView.Error.DIFFERENT_APP_PINS);
            return;
        }
        mModel.changeAppPin(Integer.getInteger(pin));
    }

    @Override
    public void changeClientPin(String pin) {
        mModel.changeClientPin(Integer.getInteger(pin));
    }
}
