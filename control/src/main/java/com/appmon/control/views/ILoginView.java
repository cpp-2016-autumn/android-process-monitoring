package com.appmon.control.views;

public interface ILoginView extends IBaseView {
    void showProgress(boolean state);
    void showInvalidUserError();
    void showWrongPasswordError();
    void startDeviceListActivity();
}
