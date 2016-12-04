package com.appmon.control.models.user;

import android.support.annotation.Nullable;

import com.appmon.control.models.IBaseModel;
import com.appmon.control.models.IFallibleListener;

/**
 * User Model interface. Implementor must handle all user management
 * business logic and register/unregister listeners
 */
public interface IUserModel extends IBaseModel {

    // provided interfaces for listeners

    interface ISignInListener extends IFallibleListener<SignInError> {}
    interface IRegisterListener extends IFallibleListener<RegisterError> {}
    interface IChangePasswordListener extends IFallibleListener<ChangePasswordError> {}
    interface IChangeAppPinListener extends IFallibleListener<ChangeAppPinError> {}
    interface IChangeClientPinListener extends IFallibleListener<ChangeClientPinError> {}
    interface IResetPasswordListener extends IFallibleListener<ResetPasswordError> {}

    // provided error enumerations

    enum ChangeAppPinError { WEAK_PIN }
    enum ChangeClientPinError { WEAK_PIN, INTERNAL_ERROR }
    enum ChangePasswordError { WEAK_PASSWORD, REAUTH_NEEDED, INTERNAL_ERROR }
    enum RegisterError { WEAK_PASSWORD, INVALID_EMAIL, USER_EXISTS, INTERNAL_ERROR }
    enum SignInError {INVALID_EMAIL, WRONG_PASSWORD, INTERNAL_ERROR }
    enum ResetPasswordError { INVALID_USER, INTERNAL_ERROR }

    // async actions

    /**
     * Signs in user with given email and password.
     * Returns results trough {@link ISignInListener}
     * Add listener with {@link #addSignInListener(ISignInListener)}
     * @param email
     * @param password
     */
    void signInWithEmail(String email, String password);

    /**
     * Registers user with given email and password.
     * Returns results trough {@link IRegisterListener}.
     * Add listener with {@link #addRegisterListener(IRegisterListener)}
     * @param email
     * @param password
     */
    void registerWithEmail(String email, String password);

    /**
     * Signs user out. Will be executed immediately.
     */
    void signOut();

    /**
     * Changes user password.
     * Returns results trough {@link IChangePasswordListener}
     * Add listener with {@link #addChangePasswordListener(IChangePasswordListener)}
     * @param password
     */
    void changePassword(String password);

    /**
     * Changes application pin-code.
     * Returns results trough {@link IChangeAppPinListener}
     * Add listener with {@link #addChangeAppPinListener(IChangeAppPinListener)}
     * @param pin new pin. can be null if pin must be cleared
     */
    void changeAppPin(String pin);

    /**
     * Changes client pin-code.
     * Returns results trough {@link IChangeClientPinListener}
     * Add listener with {@link #addChangeClientPinListener(IChangeClientPinListener)}
     * @param pin
     */
    void changeClientPin(String pin);

    /**
     * Sends email with password reset link.
     * Triggers {@link IResetPasswordListener} when finished.
     * Add listener with {@link #addResetPasswordListener(IResetPasswordListener)}
     * @param email
     */
    void resetPassword(String email);

    // sync actions

    /**
     * Returns current user ID if signed in, or null in other cases
     */
    @Nullable String getUserID();

    /**
     * Returns current application pn or null if not set
     * @return
     */
    @Nullable String getAppPin();

    // listeners setters

    void addSignInListener(ISignInListener listener);
    void addRegisterListener(IRegisterListener listener);
    void addChangePasswordListener(IChangePasswordListener listener);
    void addChangeAppPinListener(IChangeAppPinListener listener);
    void addChangeClientPinListener(IChangeClientPinListener listener);
    void addResetPasswordListener(IResetPasswordListener listener);

    // listeners removers

    void removeSignInListener(ISignInListener listener);
    void removeRegisterListener(IRegisterListener listener);
    void removeChangePasswordListener(IChangePasswordListener listener);
    void removeChangeAppPinListener(IChangeAppPinListener listener);
    void removeChangeClientPinListener(IChangeClientPinListener listener);
    void removeResetPasswordListener(IResetPasswordListener listener);
}
