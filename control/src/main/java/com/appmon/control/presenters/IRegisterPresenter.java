package com.appmon.control.presenters;

import com.appmon.control.views.IRegisterView;

/**
 * Register activity presenter interface
 */
public interface IRegisterPresenter extends IBasePresenter<IRegisterView> {
    /**
     * Register user with given email and password
     * @param email
     * @param password
     */
    void registerWithEmail(String email, String password);
}
