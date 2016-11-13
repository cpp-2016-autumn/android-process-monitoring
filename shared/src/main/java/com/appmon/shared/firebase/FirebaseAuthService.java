package com.appmon.shared.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appmon.shared.IAuthService;
import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
import com.appmon.shared.exceptions.AuthEmailTakenException;
import com.appmon.shared.exceptions.AuthException;
import com.appmon.shared.exceptions.AuthInvalidEmailException;
import com.appmon.shared.exceptions.AuthWeakPasswordException;
import com.appmon.shared.exceptions.AuthWrongPasswordException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
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
                                @Nullable final ResultListener<IUser, Throwable> listener) {
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
                    listener.onFailure(toSignInError(task.getException()));
                }
            }
        });
    }


    @Override
    public void registerWithEmail(final String email, final String password,
                                  @Nullable final ResultListener<IUser, Throwable> listener) {
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
                    listener.onFailure(toRegisterError(task.getException()));
                }
            }
        });
    }

    @Override
    public void resetPassword(final String email,
                              @Nullable final ResultListener<Void, Throwable> listener) {
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
                    listener.onFailure(toPasswordError(task.getException()));
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


    private Throwable toSignInError(Exception e) {
        Throwable err = new AuthException(e.getCause());
        if (e instanceof FirebaseAuthInvalidUserException) {
            err = new AuthInvalidEmailException(e.getCause());
        } else if (e instanceof
                FirebaseAuthInvalidCredentialsException) {
            err = new AuthWrongPasswordException(e.getCause());
        }
        return err;
    }

    private Throwable toRegisterError(Exception e) {
        Throwable err = new AuthException(e.getCause());
        if (e instanceof FirebaseAuthWeakPasswordException) {
            err = new AuthWeakPasswordException(e.getCause());
        } else if (e instanceof
                FirebaseAuthInvalidCredentialsException) {
            err = new AuthInvalidEmailException(e.getCause());
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            err = new AuthEmailTakenException(e.getCause());
        }
        return err;
    }

    private Throwable toPasswordError(Exception e) {
        Throwable err = new AuthException(e.getCause());
        if (e instanceof FirebaseAuthInvalidUserException) {
            err = new AuthInvalidEmailException(e.getCause());
        }
        return err;
    }

}
