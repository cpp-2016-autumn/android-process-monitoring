package com.appmon.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appmon.control.persistence.ModelPresenterManager;
import com.appmon.control.presenters.ILoginPresenter;
import com.appmon.control.views.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    private ILoginPresenter mPresenter;

    private ProgressBar mProgressBar;
    private View mLoginForm;
    private EditText mEmailField;
    private EditText mPasswordField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // attach presenter
        mPresenter = ModelPresenterManager.getInstance().getLoginPresenter();
        mPresenter.attachView(this);
        // Gui elements Binding
        mProgressBar = (ProgressBar) findViewById(R.id.loginProgress);
        mProgressBar.setVisibility(View.GONE);
        mLoginForm = findViewById(R.id.loginForm);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        final Button signInButton = (Button) findViewById(R.id.emailSignInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.signInWithEmail(
                        mEmailField.getText().toString(),
                        mPasswordField.getText().toString());
            }
        });
        Button passwordResetBtn = (Button) findViewById(R.id.forgotPasswordBtn);
        passwordResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.resetPassword(mEmailField.getText().toString());
            }
        });
        // Bind action to SignIn button click
        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                signInButton.callOnClick();
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
    }

    @Override
    public void startDeviceListActivity() {
        Intent deviceListActivity = new Intent(this, DeviceListActivity.class);
        deviceListActivity.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(deviceListActivity);
        finish();
    }

    @Override
    public void showProgress(boolean state) {
        if (state) {
            mLoginForm.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mLoginForm.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(Message msg) {
        switch (msg) {
            case PASSWORD_RESET_SENT:
                Toast.makeText(this, R.string.text_password_reset_sent, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showInputError(InputError err) {
        switch (err) {
            case INVALID_EMAIL:
                mEmailField.setError(getString(R.string.text_user_does_not_exist));
                mEmailField.requestFocus();
                break;
            case WRONG_PASSWORD:
                mPasswordField.setError(getString(R.string.text_wrong_password));
                mPasswordField.requestFocus();
                break;
        }
    }

    @Override
    public void clearInputErrors() {
        mEmailField.setError(null);
        mPasswordField.setError(null);
    }
}

