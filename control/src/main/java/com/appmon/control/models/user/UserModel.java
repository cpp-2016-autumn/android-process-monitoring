package com.appmon.control.models.user;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.appmon.control.utils.Validator;

import java.util.HashSet;
import java.util.Set;

/*
 * NOTE: Model implementation at the moment is at most dummy,
 * it just validates input for format correctness
 */
public class UserModel implements IUserModel {

    // listener sets
    private Set<IRegisterListener> mRegisterListeners = new HashSet<>();
    private Set<ISignInListener> mSignInListeners = new HashSet<>();
    private Set<IChangePasswordListener> mChangePasswordListeners = new HashSet<>();
    private Set<IChangeAppPinListener> mChangeAppPinListeners = new HashSet<>();
    private Set<IChangeClientPinListener> mChangeClientPinListener = new HashSet<>();
    private Set<IResetPasswordListener> mResetPasswordListeners = new HashSet<>();

    private SharedPreferences mAndroidPref;

    public UserModel(SharedPreferences androidPref) {
        mAndroidPref = androidPref;
    }

    @Override
    public void signOut() {
        // NOTE: For testing
        mAndroidPref.edit().putBoolean("signedIn", false).apply();
    }

    @Override
    public void registerWithEmail(String email, String password) {
        // early validation before firebase request
        // determine error and notify all listeners if it happened
        RegisterError err = null;
        if (!Validator.validateEmail(email)) {
            err = RegisterError.INVALID_EMAIL;
        } else if (!Validator.validatePassword(password)) {
            err = RegisterError.WEAK_PASSWORD;
        }
        if (err != null) {
            for (IRegisterListener l : mRegisterListeners) {
                l.onFail(err);
            }
            return;
        }
        // TODO: Firebase register
        mAndroidPref.edit().putBoolean("signedIn", true).apply();
        for (IRegisterListener l : mRegisterListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void signInWithEmail(String email, String password) {
        // early validation before firebase request
        SignInError err = null;
        if (!Validator.validateEmail(email)) {
            err = SignInError.INVALID_EMAIL;
        } else if (!Validator.validatePassword(password)) {
            err = SignInError.WRONG_PASSWORD;
        }
        if (err != null) {
            for (ISignInListener l : mSignInListeners) {
                l.onFail(err);
            }
            return;
        }
        // TODO: Firebase sign in
        mAndroidPref.edit().putBoolean("signedIn", true).apply();
        for (ISignInListener l : mSignInListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void changePassword(String password) {
        // early validation
        if (!Validator.validatePassword(password)) {
            for (IChangePasswordListener l : mChangePasswordListeners) {
                l.onFail(ChangePasswordError.WEAK_PASSWORD);
            }
            return;
        }
        // TODO: Firebase change password
        for (IChangePasswordListener l : mChangePasswordListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void changeAppPin(String pin) {
        // validation
        if (!Validator.validatePin(pin)) {
            for (IChangeAppPinListener l : mChangeAppPinListeners) {
                l.onFail(ChangeAppPinError.WEAK_PIN);
            }
            return;
        }
        // TODO: change App pin
        for (IChangeAppPinListener l : mChangeAppPinListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void changeClientPin(String pin) {
        // early validation
        if (!Validator.validatePin(pin)) {
            for (IChangeClientPinListener l : mChangeClientPinListener) {
                l.onFail(ChangeClientPinError.WEAK_PIN);
            }
        }
        // TODO: Write pin to Firebase
        for (IChangeClientPinListener l : mChangeClientPinListener) {
            l.onSuccess();
        }
    }

    @Override
    public void resetPassword(String email) {
        if (!Validator.validateEmail(email)) {
            for (IResetPasswordListener l : mResetPasswordListeners) {
                l.onFail(ResetPasswordError.INVALID_USER);
            }
            return;
        }
        // TODO: Firebase reset
        for (IResetPasswordListener l: mResetPasswordListeners) {
            l.onSuccess();
        }
    }

    @Nullable
    @Override
    public String getUserID() {
        // TODO: Firebase get UID
        if (mAndroidPref.getBoolean("signedIn", false)) {
            return "UID";
        }
        return null;
    }

    //======= Listeners Add\Remove operations

    @Override
    public void addSignInListener(ISignInListener listener) {
        mSignInListeners.add(listener);
    }

    @Override
    public void addRegisterListener(IRegisterListener listener) {
        mRegisterListeners.add(listener);
    }

    @Override
    public void addChangePasswordListener(IChangePasswordListener listener) {
        mChangePasswordListeners.add(listener);
    }

    @Override
    public void addChangeAppPinListener(IChangeAppPinListener listener) {
        mChangeAppPinListeners.add(listener);
    }

    @Override
    public void addChangeClientPinListener(IChangeClientPinListener listener) {
        mChangeClientPinListener.add(listener);
    }

    @Override
    public void addResetPasswordListener(IResetPasswordListener listener) {
        mResetPasswordListeners.add(listener);
    }

    @Override
    public void removeSignInListener(ISignInListener listener) {
        mSignInListeners.remove(listener);
    }

    @Override
    public void removeRegisterListener(IRegisterListener listener) {
        mRegisterListeners.remove(listener);
    }

    @Override
    public void removeChangePasswordListener(IChangePasswordListener listener) {
        mChangePasswordListeners.remove(listener);
    }

    @Override
    public void removeChangeAppPinListener(IChangeAppPinListener listener) {
        mChangeAppPinListeners.remove(listener);
    }

    @Override
    public void removeChangeClientPinListener(IChangeClientPinListener listener) {
        mChangeClientPinListener.remove(listener);
    }

    @Override
    public void removeResetPasswordListener(IResetPasswordListener listener) {
        mResetPasswordListeners.remove(listener);
    }
}
