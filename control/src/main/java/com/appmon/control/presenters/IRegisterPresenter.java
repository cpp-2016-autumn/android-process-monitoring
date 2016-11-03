package com.appmon.control.presenters;

import com.appmon.control.views.IRegisterView;

public interface IRegisterPresenter extends IBasePresenter<IRegisterView> {
    void registerWithEmail(String email, String password);
}
