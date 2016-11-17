package com.appmon.control.views;

public interface IWelcomeView extends IBaseView {
    void setPinFormVisibility(boolean state);
    void startDeviceListActivity();
    void startLoginActivity();
    void startRegisterActivity();
}
