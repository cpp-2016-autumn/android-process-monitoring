package com.appmon.control.presenters;


import com.appmon.control.views.IWelcomeView;

public interface IWelcomePresenter extends IBasePresenter<IWelcomeView> {
    void checkUserState();
    void postPin(String pin);
    void login();
    void logOut();
    void register();
}
