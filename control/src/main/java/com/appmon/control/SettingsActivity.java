package com.appmon.control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appmon.control.persistence.ModelManager;
import com.appmon.control.presenters.ISettingsPresenter;
import com.appmon.control.presenters.SettingsPresenter;
import com.appmon.control.views.ISettingsView;

public class SettingsActivity extends AppCompatActivity implements ISettingsView {

    ISettingsPresenter mPresenter;

    EditText mPasswordField;
    EditText mRepeatPasswordField;
    EditText mAppPinField;
    EditText mRepeatAppPinField;
    EditText mClientPinField;
    View mSettingsForm;
    ProgressBar mProgressBar;

    InputMethodManager mInputManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mProgressBar = (ProgressBar) findViewById(R.id.settingsProgress);
        mSettingsForm = findViewById(R.id.settingsForm);
        mSettingsForm.requestFocus();
        mInputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        // bind presenter
        mPresenter = new SettingsPresenter(ModelManager.getInstance().getUserModel());
        mPresenter.attachView(this);
        // bind gui elements
        mPasswordField = (EditText) findViewById(R.id.password);
        mRepeatPasswordField = (EditText) findViewById(R.id.passwordRepeat);
        mAppPinField = (EditText) findViewById(R.id.appPin);
        mRepeatAppPinField = (EditText) findViewById(R.id.appPinRepeat);
        mClientPinField = (EditText) findViewById(R.id.clientPin);
        // buttons
        Button signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.signOut();
            }
        });
        final Button changePasswordBtn = (Button) findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changePassword(
                        mPasswordField.getText().toString(),
                        mRepeatPasswordField.getText().toString());
            }
        });
        final Button changeAppPinBtn = (Button) findViewById(R.id.changeAppPinBtn);
        changeAppPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changeAppPin(
                        mAppPinField.getText().toString(),
                        mRepeatAppPinField.getText().toString());
            }
        });
        final Button changeClientPinBtn = (Button) findViewById(R.id.changeClientPinBtn);
        changeClientPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changeClientPin(mClientPinField.getText().toString());
            }
        });
        // bind actions
        mRepeatPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                changePasswordBtn.callOnClick();
                return true;
            }
        });
        mRepeatAppPinField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                changeAppPinBtn.callOnClick();
                return true;
            }
        });
        mClientPinField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                changeClientPinBtn.callOnClick();
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    public void showMessage(Message msg) {
        switch (msg) {
            case APP_PIN_CHANGED:
                Toast.makeText(this, R.string.msg_app_pin_changed, Toast.LENGTH_SHORT)
                        .show();
                break;
            case CLIENT_PIN_CHANGED:
                Toast.makeText(this, R.string.msg_client_pin_changed, Toast.LENGTH_SHORT).show();
                break;
            case PASSWORD_CHANGED:
                Toast.makeText(this, R.string.msg_password_changed, Toast.LENGTH_SHORT).show();
                break;
            case NETWORK_ERROR:
                Toast.makeText(this, R.string.text_network_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void showInputError(InputError err) {
        switch (err) {
            case WEAK_PASSWORD:
                mPasswordField.setError(getString(R.string.text_weak_password));
                mPasswordField.requestFocus();
                break;
            case DIFFERENT_PASSWORDS:
                mRepeatPasswordField.setError(getString(R.string.text_different_passwords));
                mRepeatPasswordField.requestFocus();
                break;
            case WEAK_APP_PIN:
                mAppPinField.setError(getString(R.string.text_weak_pin));
                mAppPinField.requestFocus();
                break;
            case DIFFERENT_APP_PINS:
                mRepeatAppPinField.setError(getString(R.string.text_different_pins));
                mRepeatAppPinField.requestFocus();
                break;
            case WEAK_CLIENT_PIN:
                mClientPinField.setError(getString(R.string.text_weak_pin));
                mClientPinField.requestFocus();
                break;
        }
    }

    @Override
    public void clearInputErrors() {
        mPasswordField.setError(null);
        mRepeatPasswordField.setError(null);
        mAppPinField.setError(null);
        mRepeatAppPinField.setError(null);
        mClientPinField.setError(null);
    }

    @Override
    public void startWelcomeActivity() {
        Intent deviceListActivity = new Intent(this, WelcomeActivity.class);
        deviceListActivity.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(deviceListActivity);
        finish();
    }

    @Override
    public void clearFocus() {
        View view = this.getCurrentFocus();
        if (view != null) {
            mInputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void setProgressVisible(boolean value) {
        if (value) {
            mSettingsForm.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mSettingsForm.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
