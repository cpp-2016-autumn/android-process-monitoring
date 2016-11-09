package com.appmon.shared;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Created by Mike on 11/9/2016.
 */

public class FirebaseAuthService implements IAuthService {

    private FirebaseAuth mAuth;
    private IUser mUser = null;

    public FirebaseAuthService() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signInWithEmail(final String email, final String password, @Nullable final ResultListener<IUser, SignInError> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = new com.appmon.shared.FirebaseUser(mAuth.getCurrentUser(), EmailAuthProvider.getCredential(email, password));
                            listener.onSuccess(mUser);
                        } else {
                            mUser = null;
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                listener.onFailure(SignInError.INVALID_EMAIL);
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                listener.onFailure(SignInError.WRONG_PASSWORD);
                            }
                        }

                    }
                });
    }

    @Override
    public void registerWithEmail(final String email, final String password, @Nullable final ResultListener<IUser, RegisterError> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = new com.appmon.shared.FirebaseUser(mAuth.getCurrentUser(), EmailAuthProvider.getCredential(email, password));
                            listener.onSuccess(mUser);
                        } else {
                            mUser = null;
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                listener.onFailure(RegisterError.WEAK_PASSWORD);
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                listener.onFailure(RegisterError.INVALID_EMAIL);
                            } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                listener.onFailure(RegisterError.USER_EXISTS);
                            }
                        }
                    }
                });
    }

    @Override
    public void resetPassword(final String email, @Nullable final ResultListener<Void, ResetPasswordError> listener) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess(null);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                listener.onFailure(ResetPasswordError.INVALID_EMAIL);
                            }
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }

    @Nullable
    @Override
    public IUser getUser() {
        return mUser;
    }
}
