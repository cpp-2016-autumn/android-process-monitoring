package com.appmon.control.views;

/**
 * Created by pacmancoder on 01.11.16.
 */

public interface ILoginView extends IBaseView {
    void showProgress();
    void hideProgress();
    void showInvalidUserError();
    void showWrongPasswordError();
    void startDeviceListActivity();
}
