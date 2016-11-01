package com.appmon.control.models;


public interface IUserModel {
    abstract class Presenter {
        public void onAuthSuccess() {}
        public void onSignedOut() {}
        public void onSignInError(SignInError error) {}
        public void onRegisterError(RegisterError error) {}
    }
    void attachPresenter(Presenter presenter);
    void detachPresenter(Presenter presenter);
    void signInWithEmail(String login, String password);
    void registerWithEmail(String login, String password);
    void signOut();
    String getUserID();
}
