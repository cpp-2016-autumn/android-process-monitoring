package com.appmon.control.views;

public interface ILoginView extends IBaseView {
    /**
     * Error kind, passed using {@link #showInputError(InputError)}
     */
    enum InputError {
        INVALID_EMAIL,
        WRONG_PASSWORD,
    }

    /**
     * Error kind, passed using {@link #showMessage(Message)}
     */
    enum Message {
        PASSWORD_RESET_SENT,
    }

    /**
     * Triggered when progress must be showed or hidden
     * @param state new progress state
     */
    void showProgress(boolean state);

    /**
     * Triggered when message must me showed
     * @param msg mwssage kind
     */
    void showMessage(Message msg);

    /**
     * Triggered when input error was happened
     * @param err error kind
     */
    void showInputError(InputError err);

    /**
     * Triggered when all errors must be hidden from view
     */
    void clearInputErrors();

    /**
     * Triggered when view must start device list activity
     */
    void startDeviceListActivity();
}
