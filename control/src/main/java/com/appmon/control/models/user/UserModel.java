package com.appmon.control.models.user;

import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class UserModel implements IUserModel {

    //private IAppServer mAuthService;

    private Set<IRegisterListener> mRegisterListeners = new HashSet<>();
    private Set<ISignInListener> mSignInListeners = new HashSet<>();
    private Set<ISignOutListener> mSignOutListeners = new HashSet<>();

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
        for (ISignInListener l : mSignInListeners) {
            l.onSuccess();
        }
    }

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

    @Nullable
    @Override
    public String getUserID() {
        return null;
    }

}
