package com.appmon.shared.firebase;

import android.support.annotation.NonNull;

import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Implementation of IUser using Firebase API
 */
class FirebaseUser implements IUser {

    private com.google.firebase.auth.FirebaseUser mUser;

    FirebaseUser(com.google.firebase.auth.FirebaseUser user){
        mUser = user;
    }

    @NonNull
    @Override
    public String getUserID() {
        return mUser.getUid();
    }

    @Override
    public void changePassword(String password,
                               final ResultListener<Void, ChangePasswordError> listener) {
        Task<Void> task = mUser.updatePassword(password);
        if (listener == null) {
            return;
        }
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess(null);
                } else {
                    if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                        listener.onFailure(ChangePasswordError.WEAK_PASSWORD);
                    } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        listener.onFailure(ChangePasswordError.INVALID_USER);
                    }
                }
            }
        });
    }
}
