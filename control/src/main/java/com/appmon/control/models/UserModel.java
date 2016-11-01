package com.appmon.control.models;

import java.util.ArrayList;
import java.util.List;

public class UserModel implements IUserModel {

    //private IAppServer mAuthService;

    private List<Presenter> mPresenters = new ArrayList<>();

    public UserModel(/*IAppServer authService*/) {
        //mAuthService = authService;
    }

    @Override
    public void attachPresenter(Presenter presenter) {
        mPresenters.add(presenter);
    }

    @Override
    public void detachPresenter(Presenter presenter) {
        mPresenters.remove(presenter);
    }

    @Override
    public void signInWithEmail(String login, String password) {
        if (login.equals("error")) {
            for (Presenter presenter : mPresenters) {
                presenter.onSignInError(SignInError.INVALID_USER);
            }
            return;
        }
        for (Presenter presenter : mPresenters) {
            presenter.onAuthSuccess();
        }
    }

    @Override
    public void registerWithEmail(String login, String password) {

    }

    @Override
    public void signOut() {}

    @Override
    public String getUserID() {
        return null;
    }
}
