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

import com.appmon.control.persistence.ModelManager;
import com.appmon.control.presenters.IRegisterPresenter;
import com.appmon.control.presenters.RegisterPresenter;
import com.appmon.control.views.IRegisterView;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    IRegisterPresenter mPresenter;

    private ProgressBar mProgressBar;
    private View mRegisterForm;
    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // attach presenter
        mPresenter = new RegisterPresenter(ModelManager.getInstance().getUserModel());
        mPresenter.attachView(this);
        // Gui elements Binding
        mProgressBar = (ProgressBar) findViewById(R.id.registerProgress);
        mProgressBar.setVisibility(View.GONE);
        mRegisterForm = findViewById(R.id.registerForm);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        final Button registerButton = (Button) findViewById(R.id.emailRegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.registerWithEmail(
                        mEmailField.getText().toString(),
                        mPasswordField.getText().toString());
            }
        });
        // Bind action to SignIn button click
        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                registerButton.callOnClick();
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
    public void showInputError(InputError err) {
        switch (err) {
            case INVALID_EMAIL:
                mEmailField.setError(getString(R.string.text_invalid_email));
                mEmailField.requestFocus();
                break;
            case USER_EXISTS:
                mEmailField.setError(getString(R.string.text_user_exists));
                mEmailField.requestFocus();
                break;
            case WEAK_PASSWORD:
                mPasswordField.setError(getString(R.string.text_weak_password));
                mPasswordField.requestFocus();
                break;
        }
    }

    @Override
    public void showProgress(boolean state) {
        if (state) {
            mRegisterForm.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mRegisterForm.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void clearInputErrors() {
        mEmailField.setError(null);
        mPasswordField.setError(null);
    }
}
