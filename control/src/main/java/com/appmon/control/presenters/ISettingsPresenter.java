package com.appmon.control.presenters;

import com.appmon.control.views.ISettingsView;

/**
 * Settings activity interface
 */
public interface ISettingsPresenter extends IBasePresenter<ISettingsView> {
    /**
     * Changes password
     * @param password new password
     * @param passwordRepeat password, entered in "repeat password" field.
     */
    void changePassword(String password, String passwordRepeat);

    /**
     * Signs user out
     */
    void signOut();

    /**
     * changes application pin
     * @param pin new pin
     * @param pinRepeat pin, entered in "repeat pin" field.
     */
    void changeAppPin(String pin, String pinRepeat);

    /**
     * changes client devices pin
     * @param pin new pin
     */
    void changeClientPin(String pin);
}
