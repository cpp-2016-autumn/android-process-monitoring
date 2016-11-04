package com.appmon.control.views;

public interface IRegisterView extends IBaseView {
    enum Error {
        INVALID_EMAIL,
        WEAK_PASSWORD,
        USER_EXISTS,
    }
    void showProgress(boolean state);
    void showError(Error err);
    void clearErrors();
    void startDeviceListActivity();
}
