package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.ISettingsView;

public class SettingsPresenter implements ISettingsPresenter {

    private IUserModel mModel;
    private ISettingsView mView = null;

    private IUserModel.IChangePasswordListener mChangePasswordListener = null;
    private IUserModel.IChangeAppPinListener mChangeAppPinListener = null;
    private IUserModel.IChangeClientPinListener mChangeClientPinListener = null;

    public SettingsPresenter(IUserModel model) {
        mModel = model;
        mChangePasswordListener = new IUserModel.IChangePasswordListener() {
            @Override
            public void onSuccess() {
                mView.showMessage(ISettingsView.Message.PASSWORD_CHANGED);
            }

            @Override
            public void onFail(IUserModel.ChangePasswordError error) {
                switch (error) {
                    case WEAK_PASSWORD:
                        mView.showInputError(ISettingsView.InputError.WEAK_PASSWORD);
                        break;
                }
            }
        };

        mChangeAppPinListener = new IUserModel.IChangeAppPinListener() {
            @Override
            public void onSuccess() {
                mView.showMessage(ISettingsView.Message.APP_PIN_CHANGED);
            }

            @Override
            public void onFail(IUserModel.ChangeAppPinError error) {
                switch (error) {
                    case WEAK_PIN:
                        mView.showInputError(ISettingsView.InputError.WEAK_APP_PIN);
                        break;
                }
            }
        };

        mChangeClientPinListener = new IUserModel.IChangeClientPinListener() {
            @Override
            public void onSuccess() {
                mView.showMessage(ISettingsView.Message.CLIENT_PIN_CHANGED);
            }

            @Override
            public void onFail(IUserModel.ChangeClientPinError error) {
                switch (error) {
                    case WEAK_PIN:
                        mView.showInputError(ISettingsView.InputError.WEAK_CLIENT_PIN);
                        break;
                }
            }
        };
    }

    @Override
    public void attachView(ISettingsView view) {
        mView = view;
        mModel.addChangePasswordListener(mChangePasswordListener);
        mModel.addChangeAppPinListener(mChangeAppPinListener);
        mModel.addChangeClientPinListener(mChangeClientPinListener);
    }

    @Override
    public void detachView() {
        mView = null;
        mModel.removeChangePasswordListener(mChangePasswordListener);
        mModel.removeChangeAppPinListener(mChangeAppPinListener);
        mModel.removeChangeClientPinListener(mChangeClientPinListener);
    }

    @Override
    public void changePassword(String password, String passwordRepeat) {
        mView.clearInputErrors();
        if (!password.equals(passwordRepeat)) {
            mView.showInputError(ISettingsView.InputError.DIFFERENT_PASSWORDS);
            return;
        }
        mModel.changePassword(password);
    }

    @Override
    public void signOut() {
        mModel.signOut();
        mView.clearInputErrors();
        mView.startWelcomeActivity();
    }

    @Override
    public void changeAppPin(String pin, String pinRepeat) {
        mView.clearInputErrors();
        if (!pin.equals(pinRepeat)) {
            mView.showInputError(ISettingsView.InputError.DIFFERENT_APP_PINS);
            return;
        }
        mModel.changeAppPin(Integer.parseInt(pin));
    }

    @Override
    public void changeClientPin(String pin) {
        mView.clearInputErrors();
        mModel.changeClientPin(Integer.parseInt(pin));
    }
}
