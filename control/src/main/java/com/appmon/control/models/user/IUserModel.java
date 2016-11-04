package com.appmon.control.models.user;

import android.support.annotation.Nullable;

import com.appmon.control.models.IBaseModel;
import com.appmon.control.models.IFallibleListener;

public interface IUserModel extends IBaseModel {

    interface ISignInListener extends IFallibleListener<SignInError> {}
    interface IRegisterListener extends IFallibleListener<RegisterError> {}
    interface ISignOutListener extends IFallibleListener<SignOutError> {}
    interface IChangePasswordListener extends IFallibleListener<ChangePasswordError> {}
    interface IChangeAppPinListener extends IFallibleListener<ChangeAppPinError> {}
    interface IChangeClientPinListener extends IFallibleListener<ChangeClientPinError> {}

    void signInWithEmail(String login, String password);
    void registerWithEmail(String login, String password);
    void signOut();
    void changePassword(String password);
    void changeAppPin(int pin);
    void changeClientPin(int pin);

    void addSignInListener(ISignInListener listener);
    void addRegisterListener(IRegisterListener listener);
    void addSignOutListener(ISignOutListener listener);
    void addChangePasswordListener(IChangePasswordListener listener);
    void addChangeAppPinListener(IChangeAppPinListener listener);
    void addChangeClientPinListener(IChangeClientPinListener listener);

    void removeSignInListener(ISignInListener listener);
    void removeRegisterListener(IRegisterListener listener);
    void removeSignOutListener(ISignOutListener listener);
    void removeChangePasswordListener(IChangePasswordListener listener);
    void removeChangeAppPinListener(IChangeAppPinListener listener);
    void removeChangeClientPinListener(IChangeClientPinListener listener);
    @Nullable String getUserID();
}
