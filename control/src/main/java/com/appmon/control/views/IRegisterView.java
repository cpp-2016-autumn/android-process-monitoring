package com.appmon.control.views;

public interface IRegisterView {
    void showProgress();
    void hideProgress();
    void showInvalidUserError();
    void showWeakPasswordError();
    void showUserExistsError();
    void startDeviceListActivity();
}
