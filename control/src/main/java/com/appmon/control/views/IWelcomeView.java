package com.appmon.control.views;

/**
 * Welcome View interface
 */
public interface IWelcomeView extends IBaseView {
    /**
     * Triggered when pin enter form must be showed or hidden
     * @param state
     */
    void setPinFormVisibility(boolean state);

    /**
     * Triggered when device activity must be started
     */
    void startDeviceListActivity();

    /**
     * Triggered when login activity must be started
     */
    void startLoginActivity();

    /**
     * Triggered when register activity must be started
     */
    void startRegisterActivity();
}
