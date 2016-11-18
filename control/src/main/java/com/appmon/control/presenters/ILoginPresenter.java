package com.appmon.control.presenters;

import com.appmon.control.views.ILoginView;

/**
 * Login Activity interface
 */
public interface ILoginPresenter extends IBasePresenter<ILoginView> {
    /**
     * Signs in user using email and password
     * @param email
     * @param password
     */
    void signInWithEmail(String email, String password);

    /**
     * requests password reset email sending
     * @param email user email, where message must be sent
     */
    void resetPassword(String email);
}
