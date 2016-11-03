package com.appmon.control.presenters;

import com.appmon.control.views.ILoginView;

/**
 * Created by pacmancoder on 01.11.16.
 */

public interface ILoginPresenter extends IBasePresenter<ILoginView> {
    void signInWithEmail(String email, String password);
}
