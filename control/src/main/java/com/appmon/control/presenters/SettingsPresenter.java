package com.appmon.control.presenters;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.views.ISettingsView;

/**
 * Settings presenter class.
 * IUserModel dependency.
 */
public class SettingsPresenter implements ISettingsPresenter {

    private IUserModel mModel;
    private ISettingsView mView = null;

    private IUserModel.IChangePasswordListener mChangePasswordListener = null;
    private IUserModel.IChangeAppPinListener mChangeAppPinListener = null;
    private IUserModel.IChangeClientPinListener mChangeClientPinListener = null;

    public SettingsPresenter(IUserModel model) {
        mModel = model;
        // init change password listener
        mChangePasswordListener = new IUserModel.IChangePasswordListener() {
            @Override
            public void onSuccess() {
                mView.setProgressVisible(false);
                mView.clearFocus();
                mView.showMessage(ISettingsView.Message.PASSWORD_CHANGED);
            }

            @Override
            public void onFail(IUserModel.ChangePasswordError error) {
                mView.setProgressVisible(false);
                switch (error) {
                    case WEAK_PASSWORD:
                        mView.showInputError(ISettingsView.InputError.WEAK_PASSWORD);
                        break;
                }
            }
        };
        // init change app pin listener
        mChangeAppPinListener = new IUserModel.IChangeAppPinListener() {
            @Override
            public void onSuccess() {
                mView.setProgressVisible(false);
                mView.clearFocus();
                mView.showMessage(ISettingsView.Message.APP_PIN_CHANGED);
            }

            @Override
            public void onFail(IUserModel.ChangeAppPinError error) {
                mView.setProgressVisible(false);
                switch (error) {
                    case WEAK_PIN:
                        mView.showInputError(ISettingsView.InputError.WEAK_APP_PIN);
                        break;
                }
            }
        };
        // init change client pin listener
        mChangeClientPinListener = new IUserModel.IChangeClientPinListener() {
            @Override
            public void onSuccess() {
                mView.setProgressVisible(false);
                mView.clearFocus();
                mView.showMessage(ISettingsView.Message.CLIENT_PIN_CHANGED);
            }

            @Override
            public void onFail(IUserModel.ChangeClientPinError error) {
                mView.setProgressVisible(false);
                switch (error) {
                    case WEAK_PIN:
                        mView.showInputError(ISettingsView.InputError.WEAK_CLIENT_PIN);
                        break;
                }
            }
        };
    }

    @Override
    public void changePassword(String password, String passwordRepeat) {
        if (mView != null) {
            mView.setProgressVisible(true);
            mView.clearInputErrors();
            // validation of password equality
            if (!password.equals(passwordRepeat)) {
                mView.setProgressVisible(false);
                mView.showInputError(ISettingsView.InputError.DIFFERENT_PASSWORDS);
                return;
            }
        }
        mModel.changePassword(password);
    }

    @Override
    public void signOut() {
        mModel.signOut();
        if (mView == null) return;
        mView.clearInputErrors();
        mView.startWelcomeActivity();
    }

    @Override
    public void changeAppPin(String pin, String pinRepeat) {
        if (mView != null) {
            mView.setProgressVisible(true);
            mView.clearInputErrors();
            // validation of pin equality
            if (!pin.equals(pinRepeat)) {
                mView.setProgressVisible(false);
                mView.showInputError(ISettingsView.InputError.DIFFERENT_APP_PINS);
                return;
            }
        }
        // text string from view never will be null
        mModel.changeAppPin(pin.isEmpty() ? null : pin);
    }

    @Override
    public void changeClientPin(String pin) {
        mModel.changeClientPin(pin);
        if (mView == null) return;
        mView.setProgressVisible(true);
        mView.clearInputErrors();
    }

    @Override
    public void attachView(ISettingsView view) {
        mView = view;
        // connect listeners
        mModel.addChangePasswordListener(mChangePasswordListener);
        mModel.addChangeAppPinListener(mChangeAppPinListener);
        mModel.addChangeClientPinListener(mChangeClientPinListener);
    }

    @Override
    public void detachView() {
        mView = null;
        // disconnect listeners (because no receiver view)
        mModel.removeChangePasswordListener(mChangePasswordListener);
        mModel.removeChangeAppPinListener(mChangeAppPinListener);
        mModel.removeChangeClientPinListener(mChangeClientPinListener);
    }
}
