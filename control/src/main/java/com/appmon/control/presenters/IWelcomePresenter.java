package com.appmon.control.presenters;


import com.appmon.control.views.IWelcomeView;

/**
 * Welcome presenter interface
 */
public interface IWelcomePresenter extends IBasePresenter<IWelcomeView> {
    /**
     * requests user state
     */
    void checkUserState();

    /**
     * publishes entered pin to presenter to unlock application
     * @param pin
     */
    void postPin(String pin);

    /**
     * preforms open login activity action
     */
    void login();

    /**
     * preforms open log out activity action
     */
    void logOut();

    /**
     * preforms open register activity action
     */
    void register();
}
