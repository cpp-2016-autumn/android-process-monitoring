package com.appmon.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appmon.control.persistence.ModelPresenterManager;
import com.appmon.control.presenters.ISettingsPresenter;
import com.appmon.control.views.ISettingsView;

public class SettingsActivity extends AppCompatActivity implements ISettingsView {

    ISettingsPresenter mPresenter;

    EditText mPasswordField;
    EditText mRepeatPasswordField;
    EditText mAppPinField;
    EditText mRepeatAppPinField;
    EditText mClientPinField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // bind presenter
        mPresenter = ModelPresenterManager.getInstance().getSettingsPresenter();
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
        Button changePasswordBtn = (Button) findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changePassword(
                        mPasswordField.getText().toString(),
                        mRepeatPasswordField.getText().toString());
            }
        });
        Button changeAppPinBtn = (Button) findViewById(R.id.changeAppPinBtn);
        changeAppPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changeAppPin(
                        mAppPinField.getText().toString(),
                        mRepeatAppPinField.getText().toString());
            }
        });
        Button changeClientPinBtn = (Button) findViewById(R.id.changeClientPinBtn);
        changeClientPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changeClientPin(mClientPinField.getText().toString());
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
        }
    }

    @Override
    public void showInputError(InputError err) {
        switch (err) {
            case WEAK_PASSWORD:
                mPasswordField.setError(getString(R.string.text_weak_password));
                break;
            case DIFFERENT_PASSWORDS:
                mRepeatPasswordField.setError(getString(R.string.text_different_passwords));
                break;
            case WEAK_APP_PIN:
                mAppPinField.setError(getString(R.string.text_weak_pin));
                break;
            case DIFFERENT_APP_PINS:
                mRepeatAppPinField.setError(getString(R.string.text_different_pins));
                break;
            case WEAK_CLIENT_PIN:
                mClientPinField.setError(getString(R.string.text_weak_pin));
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
}
