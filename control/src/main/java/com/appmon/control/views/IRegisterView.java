package com.appmon.control.views;

public interface IRegisterView extends IBaseView {
    enum InputError {
        INVALID_EMAIL,
        WEAK_PASSWORD,
        USER_EXISTS,
    }

    void showProgress(boolean state);
    void showInputError(InputError err);
    void clearInputErrors();
    void startDeviceListActivity();
}
