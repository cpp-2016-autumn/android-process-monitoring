package com.appmon.shared.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appmon.shared.IAuthService;
import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
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
 * Implementation {@link IAuthService} for Firebase backend.
 */
class FirebaseAuthService implements IAuthService {

    private FirebaseAuth mAuth;
    private IUser mUser = null;

    FirebaseAuthService() {
        mAuth = FirebaseAuth.getInstance();
        // construct wrapper for current signed in user, if exists
        com.google.firebase.auth.FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            mUser = new FirebaseUser(firebaseUser);
        }
    }

    @Override
    public void signInWithEmail(final String email, final String password,
                                @Nullable final ResultListener<IUser, SignInError> listener) {
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
        if (listener == null) {
            return;
        }
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = new FirebaseUser(mAuth.getCurrentUser());
                    listener.onSuccess(mUser);
                } else {
                    mUser = null;
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        listener.onFailure(SignInError.INVALID_EMAIL);
                    } else if (task.getException() instanceof
                            FirebaseAuthInvalidCredentialsException) {
                        listener.onFailure(SignInError.WRONG_PASSWORD);
                    }
                }
            }
        });
    }

    @Override
    public void registerWithEmail(final String email, final String password,
                                  @Nullable final ResultListener<IUser, RegisterError> listener) {
        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);
        if (listener == null) {
            return;
        }
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = new FirebaseUser(mAuth.getCurrentUser());
                    listener.onSuccess(mUser);
                } else {
                    mUser = null;
                    if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                        listener.onFailure(RegisterError.WEAK_PASSWORD);
                    } else if (task.getException() instanceof
                            FirebaseAuthInvalidCredentialsException) {
                        listener.onFailure(RegisterError.INVALID_EMAIL);
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        listener.onFailure(RegisterError.USER_EXISTS);
                    }
                }
            }
        });
    }

    @Override
    public void resetPassword(final String email,
                              @Nullable final ResultListener<Void, ResetPasswordError> listener) {
        Task<Void> task = mAuth.sendPasswordResetEmail(email);
        if (listener == null) {
            return;
        }
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
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
        mUser = null;
    }

    @Nullable
    @Override
    public IUser getUser() {
        return mUser;
    }
}
