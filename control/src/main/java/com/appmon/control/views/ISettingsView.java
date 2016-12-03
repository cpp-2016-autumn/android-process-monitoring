package com.appmon.control.views;

public interface ISettingsView extends IBaseView {
    /**
     * Error kind, passed by {@link #showInputError(InputError)}
     */
    enum InputError {
        WEAK_PASSWORD,
        DIFFERENT_PASSWORDS,
        WEAK_APP_PIN,
        DIFFERENT_APP_PINS,
        WEAK_CLIENT_PIN,
    }

    /**
     * Message, passed by {@link #showMessage(Message)}
     */
    enum Message {
        PASSWORD_CHANGED,
        APP_PIN_CHANGED,
        CLIENT_PIN_CHANGED,
        NETWORK_ERROR,
    }

    /**
     * Triggered when message must be shown
     * @param msg message kind
     */
    void showMessage(Message msg);

    /**
     * Triggered when error must be signaled
     * @param err error kind
     */
    void showInputError(InputError err);

    /**
     * Triggered when all errors must be hidden
     */
    void clearInputErrors();

    /**
     * Triggered then view must start welcome activity (log out)
     */
    void startWelcomeActivity();

    /**
     * Triggered when view must clear it's focus
     */
    void clearFocus();

    /**
     * Triggered when progress bust be showed or hidden
     * @param value
     */
    void setProgressVisible(boolean value);
}
