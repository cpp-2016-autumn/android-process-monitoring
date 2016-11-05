package com.appmon.control.presenters;


import com.appmon.control.views.IWelcomeView;

public interface IWelcomePresenter extends IBasePresenter<IWelcomeView> {
    void checkUserState();
    void login();
    void register();
}
