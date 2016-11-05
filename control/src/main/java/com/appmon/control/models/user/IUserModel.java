package com.appmon.control.models.user;

import android.support.annotation.Nullable;

import com.appmon.control.models.IBaseModel;
import com.appmon.control.models.IFallibleListener;

public interface IUserModel extends IBaseModel {

    // provided interfaces
    interface ISignInListener extends IFallibleListener<SignInError> {}
    interface IRegisterListener extends IFallibleListener<RegisterError> {}
    interface IChangePasswordListener extends IFallibleListener<ChangePasswordError> {}
    interface IChangeAppPinListener extends IFallibleListener<ChangeAppPinError> {}
    interface IChangeClientPinListener extends IFallibleListener<ChangeClientPinError> {}
    interface IPasswordResetListener extends IFallibleListener<PasswordResetError> {}
    // provided error enumerations
    enum ChangeAppPinError { WEAK_PIN }
    enum ChangeClientPinError { WEAK_PIN }
    enum ChangePasswordError { WEAK_PASSWORD }
    enum RegisterError { WEAK_PASSWORD, INVALID_EMAIL, USER_EXISTS }
    enum SignInError { INVALID_USER, WRONG_PASSWORD }
    enum PasswordResetError { INVALID_USER }

    void signInWithEmail(String login, String password);
    void registerWithEmail(String login, String password);
    void signOut();
    void changePassword(String password);
    void changeAppPin(int pin);
    void changeClientPin(int pin);
    void resetPassword();

    void addSignInListener(ISignInListener listener);
    void addRegisterListener(IRegisterListener listener);
    void addChangePasswordListener(IChangePasswordListener listener);
    void addChangeAppPinListener(IChangeAppPinListener listener);
    void addChangeClientPinListener(IChangeClientPinListener listener);
    void addPasswordResetListener(IPasswordResetListener listener);

    void removeSignInListener(ISignInListener listener);
    void removeRegisterListener(IRegisterListener listener);
    void removeChangePasswordListener(IChangePasswordListener listener);
    void removeChangeAppPinListener(IChangeAppPinListener listener);
    void removeChangeClientPinListener(IChangeClientPinListener listener);
    void removePasswordResetListener(IPasswordResetListener listener);

    @Nullable String getUserID();
}
