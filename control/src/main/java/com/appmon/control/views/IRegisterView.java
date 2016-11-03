package com.appmon.control.views;

public interface IRegisterView extends IBaseView {
    void showProgress(boolean state);
    void showInvalidEmailError();
    void showWeakPasswordError();
    void showUserExistsError();
    void startDeviceListActivity();
}
