package com.appmon.shared;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Implementation of IUser using Firebase API
 */
public class FirebaseUser implements IUser {
    private AuthCredential mUserCredential;
    private com.google.firebase.auth.FirebaseUser mUser;

    public FirebaseUser(com.google.firebase.auth.FirebaseUser user, AuthCredential credential){
        mUser = user;
        mUserCredential = credential;
    }

    @NonNull
    @Override
    public String getUserID() {
        return mUser.getUid();
    }

    @Override
    public void changePassword(String password, final ResultListener<Void, ChangePasswordError> listener) {
        mUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess(null);
                }else{
                    if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                        listener.onFailure(ChangePasswordError.WEAK_PASSWORD);
                    }else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        listener.onFailure(ChangePasswordError.INVALID_USER);
                    }else if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                        listener.onFailure(ChangePasswordError.FRESH_AUTH_NEEDED);
                    }
                }
            }
        });
    }

    @Override
    public void reauth() {
        mUser.reauthenticate(mUserCredential);
    }
}
