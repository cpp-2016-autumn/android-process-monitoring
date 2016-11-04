package com.appmon.control.views;

public interface ISettingsView extends IBaseView {
    enum Error {
        WEAK_PASSWORD,
        DIFFERENT_PASSWORDS,
        WEAK_APP_PIN,
        DIFFERENT_APP_PINS,
        WEAK_CLIENT_PIN,
    }
    enum Message {
        PASSWORD_CHANGED,
        APP_PIN_CHANGED,
        CLIENT_PIN_CHANGED,
    }

    void showMessage(Message msg);
    void showError(Error err);
    void clearErrors();
    void startWelcomeActivity();
}
