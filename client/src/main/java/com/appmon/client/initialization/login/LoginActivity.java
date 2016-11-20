package com.appmon.client.initialization.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appmon.client.R;
import com.appmon.client.initialization.SetupService;

/**
 * A login screen that offers login via email/password.
 * Created by MikeSotnichek on 11/1/2016.
 */
public class LoginActivity extends AppCompatActivity implements ILoginActivity {

    private ILoginController mController;

    // UI references.
    private EditText mEmailText;
    private EditText mPasswordText;
    private View mProgressView;
    private View mLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mController = LoginController.getInstance();
        mController.hookActivity(this);

        // Set up the login form.
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);
        mProgressView = findViewById(R.id.email_login_progress);
        mLoginView = findViewById(R.id.email_login_form);
        mProgressView.setVisibility(View.GONE);
        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mPasswordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                mEmailSignInButton.callOnClick();
                return true;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm =
                            (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mController.attemptLogin(
                        mEmailText.getText().toString(),
                        mPasswordText.getText().toString());
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mLoginView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            mLoginView.setVisibility(View.VISIBLE);
            mProgressView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setError(Error error) {
        switch (error) {
            case NO_ERROR:
                mPasswordText.setError(null);
                mEmailText.setError(null);
                break;
            case EMAIL_EMPTY:
                mEmailText.setError(getString(R.string.error_field_required));
                mEmailText.requestFocus();
                break;
            case EMAIL_INVALID:
                mEmailText.setError(getString(R.string.error_invalid_email));
                mEmailText.requestFocus();
                break;
            case PASSWORD_INVALID:
                mPasswordText.setError(getString(R.string.error_incorrect_password));
                mPasswordText.requestFocus();
                break;
        }
    }


    @Override
    public void loginSuccessful() {
        SetupService.StartInit(getApplicationContext());
        Toast.makeText(LoginActivity.this, R.string.auth_complete, Toast.LENGTH_LONG).show();
        finish();
    }

}

