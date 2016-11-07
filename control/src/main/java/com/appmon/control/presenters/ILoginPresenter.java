package com.appmon.control.presenters;

import com.appmon.control.views.ILoginView;

/**
 * Login Activity interface
 */
public interface ILoginPresenter extends IBasePresenter<ILoginView> {
    void signInWithEmail(String email, String password);
    void resetPassword(String email);
}
