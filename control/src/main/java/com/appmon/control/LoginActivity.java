package com.appmon.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.appmon.control.persistence.ModelPresenterManager;
import com.appmon.control.presenters.ILoginPresenter;
import com.appmon.control.views.ILoginView;
// TODO: close parrent activity on finish
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // attach presenter
        mPresenter = ModelPresenterManager.getInstance().getLoginPresenter();
        mPresenter.attachView(this);
        // Gui elements Binding
        mProgressBar = (ProgressBar) findViewById(R.id.loginProgress);
        mProgressBar.setVisibility(View.GONE);
        mLoginForm = findViewById(R.id.loginForm);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        Button signInButton = (Button) findViewById(R.id.emailSignInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.signInWithEmail(
                        mEmailField.getText().toString(),
                        mPasswordField.getText().toString());
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
        deviceListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(deviceListActivity);
        finish();
    }

    @Override
    public void showWrongPasswordError() {
        mPasswordField.setError(getString(R.string.text_wrong_password));
    }

    @Override
    public void showInvalidUserError() {
        mEmailField.setError(getString(R.string.text_user_does_not_exist));
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
}

