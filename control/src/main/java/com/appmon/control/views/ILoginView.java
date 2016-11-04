package com.appmon.control.views;

public interface ILoginView extends IBaseView {
    enum Error {
        INVALID_USER,
        WRONG_PASSWORD,
    }
    void showProgress(boolean state);
    void showError(Error err);
    void clearErrors();
    void startDeviceListActivity();
}
