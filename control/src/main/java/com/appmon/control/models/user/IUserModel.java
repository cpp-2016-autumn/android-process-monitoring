package com.appmon.control.models.user;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appmon.control.models.IBaseModel;
import com.appmon.control.models.IFallibleListener;

public interface IUserModel extends IBaseModel {

    interface ISignInListener extends IFallibleListener<SignInError> {}
    interface IRegisterListener extends IFallibleListener<RegisterError> {}
    interface ISignOutListener extends IFallibleListener<SignOutError> {}

    void signInWithEmail(String login, String password);
    void registerWithEmail(String login, String password);
    void signOut();

    void addSignInListener(ISignInListener listener);
    void addRegisterListener(IRegisterListener listener);
    void addSignOutListener(ISignOutListener listener);

    void removeSignInListener(ISignInListener listener);
    void removeRegisterListener(IRegisterListener listener);
    void removeSignOutListener(ISignOutListener listener);

    @Nullable String getUserID();
}
