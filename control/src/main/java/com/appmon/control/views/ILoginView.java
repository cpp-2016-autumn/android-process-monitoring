package com.appmon.control.views;

public interface ILoginView extends IBaseView {
    enum InputError {
        INVALID_USER,
        WRONG_PASSWORD,
    }
    enum Message {
        PASSWORD_RESET_SENT,
    }
    void showProgress(boolean state);
    void showMessage(Message msg);
    void showInputError(InputError err);
    void clearInputErrors();
    void startDeviceListActivity();
}
