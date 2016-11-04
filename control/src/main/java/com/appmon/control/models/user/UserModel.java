package com.appmon.control.models.user;

import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class UserModel implements IUserModel {

    //private IAppServer mAuthService;

    private Set<IRegisterListener> mRegisterListeners = new HashSet<>();
    private Set<ISignInListener> mSignInListeners = new HashSet<>();
    private Set<ISignOutListener> mSignOutListeners = new HashSet<>();
    private Set<IChangePasswordListener> mChangePasswordListeners = new HashSet<>();
    private Set<IChangeAppPinListener> mChangeAppPinListeners = new HashSet<>();
    private Set<IChangeClientPinListener> mChangeClientPinListener = new HashSet<>();

    public UserModel(/*IAppServer authService*/) {
        //mAuthService = authService;
    }

    @Override
    public void signOut() {
        for (ISignOutListener l : mSignOutListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void registerWithEmail(String login, String password) {
        // NOTE: For testing
        if (login.equals("error")) {
            for (IRegisterListener l : mRegisterListeners) {
                l.onFail(RegisterError.INVALID_EMAIL);
            }
            return;
        }
        for (IRegisterListener l : mRegisterListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void signInWithEmail(String login, String password) {
        // NOTE: For testing
        for (ISignInListener l : mSignInListeners) {
            l.onSuccess();
        }
    }

    @Override
    public void changePassword(String password) {
        // NOTE: For testing
        if (password.length() < 6) {
            for (IChangePasswordListener l : mChangePasswordListeners) {
                l.onFail(ChangePasswordError.WEAK_PASSWORD);
            }
        } else {
            for (IChangePasswordListener l : mChangePasswordListeners) {
                l.onSuccess();
            }
        }
    }

    @Override
    public void changeAppPin(int pin) {
        // NOTE: For testing
        if (pin <= 99 ) {
            for (IChangeAppPinListener l : mChangeAppPinListeners) {
                l.onFail(ChangeAppPinError.WEAK_PIN);
            }
        } else {
            for (IChangeAppPinListener l : mChangeAppPinListeners) {
                l.onSuccess();
            }
        }
    }

    @Override
    public void changeClientPin(int pin) {
        // NOTE: For testing
        if (pin <= 99 ) {
            for (IChangeClientPinListener l : mChangeClientPinListener) {
                l.onFail(ChangeClientPinError.WEAK_PIN);
            }
        } else {
            for (IChangeClientPinListener l : mChangeClientPinListener) {
                l.onSuccess();
            }
        }
    }


    @Nullable
    @Override
    public String getUserID() {
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
    public void addSignOutListener(ISignOutListener listener) {
        mSignOutListeners.add(listener);
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
    public void removeSignInListener(ISignInListener listener) {
        mSignInListeners.remove(listener);
    }

    @Override
    public void removeRegisterListener(IRegisterListener listener) {
        mRegisterListeners.remove(listener);
    }

    @Override
    public void removeSignOutListener(ISignOutListener listener) {
        mSignOutListeners.remove(listener);
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
}
