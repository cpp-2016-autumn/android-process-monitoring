package com.appmon.control.presenters;

import com.appmon.control.views.ISettingsView;

public interface ISettingsPresenter extends IBasePresenter<ISettingsView> {
    void changePassword(String password, String passwordRepeat);
    void signOut();
    void changeAppPin(String pin, String pinRepeat);
    void changeClientPin(String pin);
}
