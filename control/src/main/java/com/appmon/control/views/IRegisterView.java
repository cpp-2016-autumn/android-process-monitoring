package com.appmon.control.views;

/**
 * Register view interface
 */
public interface IRegisterView extends IBaseView {
    /**
     * Represents error kinds, which can be passed from {@link #showInputError(InputError)}
     */
    enum InputError {
        INVALID_EMAIL,
        WEAK_PASSWORD,
        USER_EXISTS,
    }

    /**
     * Triggered when progress must be showed or hidden
     * @param state new progress state
     */
    void showProgress(boolean state);

    /**
     * Triggered when some error happened
     * @param err error kind
     */
    void showInputError(InputError err);

    /**
     * Triggered when errors must be hidden
     */
    void clearInputErrors();

    /**
     * Triggered when Device list activity must be opened
     */
    void startDeviceListActivity();
}
