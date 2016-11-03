package com.appmon.control.presenters;


import com.appmon.control.views.IWelcomeView;

/**
 * Created by pacmancoder on 01.11.16.
 */

public interface IWelcomePresenter extends IBasePresenter<IWelcomeView> {
    void checkUserState();
    void login();
    void register();
}
