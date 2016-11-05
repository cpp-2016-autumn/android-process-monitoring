package com.appmon.control.presenters;

import com.appmon.control.views.IRegisterView;

/**
 * Register activity presenter
 */
public interface IRegisterPresenter extends IBasePresenter<IRegisterView> {
    void registerWithEmail(String email, String password);
}
