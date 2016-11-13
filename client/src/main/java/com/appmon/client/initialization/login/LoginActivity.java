package com.appmon.client.initialization.login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appmon.client.R;
import com.appmon.client.initialization.SetupService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 * Created by MikeSotnichek on 11/1/2016.
 */
public class LoginActivity extends AppCompatActivity implements ILoginActivity {

    private ILoginController mController;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mController = LoginController.getInstance();
        mController.hookActivity(this);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                mEmailSignInButton.callOnClick();
                return true;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.attemptLogin(
                        mEmailView.getText().toString(),
                        mPasswordView.getText().toString());
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void setError(Error error) {
        switch (error) {
            case NO_ERROR:
                mPasswordView.setError(null);
                mEmailView.setError(null);
                break;
            case EMAIL_EMPTY:
                mEmailView.setError(getString(R.string.error_field_required));
                mEmailView.requestFocus();
                break;
            case EMAIL_INVALID:
                mEmailView.setError(getString(R.string.error_invalid_email));
                mEmailView.requestFocus();
                break;
            case PASSWORD_INVALID:
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                break;
        }
    }


    @Override
    public void loginSuccessful(String userID) {
        SetupService.StartInit(getApplicationContext(), userID);
        Toast.makeText(LoginActivity.this, R.string.auth_complete, Toast.LENGTH_LONG).show();
        finish();
    }

}

