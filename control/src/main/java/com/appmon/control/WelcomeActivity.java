package com.appmon.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appmon.control.persistence.ModelManager;
import com.appmon.control.presenters.IWelcomePresenter;
import com.appmon.control.presenters.WelcomePresenter;
import com.appmon.control.views.IWelcomeView;

public class WelcomeActivity extends AppCompatActivity implements IWelcomeView {

    private IWelcomePresenter mPresenter;

    private EditText mPinField;
    private View mNewUserForm;
    private View mExistingUserForm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mPinField = (EditText) findViewById(R.id.pinField);
        mNewUserForm = findViewById(R.id.newUserForm);
        mExistingUserForm = findViewById(R.id.existingUserForm);
        // get presenter and attach this view to it
        mPresenter = new WelcomePresenter(ModelManager.getInstance().getUserModel());
        mPresenter.attachView(this);
        // set listeners
        Button loginButton = (Button) findViewById(R.id.welcomeLoginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login();
            }
        });
        Button registerButton = (Button) findViewById(R.id.welcomeRegisterBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register();
            }
        });
        Button logOutButton = (Button) findViewById(R.id.welcomeLogOutBtn);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.logOut();
            }
        });
        mPinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.postPin(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    @Override
    public void setPinFormVisibility(boolean state) {
        if (state) {
            mExistingUserForm.setVisibility(View.VISIBLE);
            mNewUserForm.setVisibility(View.GONE);
            mPinField.requestFocus();
        } else {
            mExistingUserForm.setVisibility(View.GONE);
            mNewUserForm.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // re-attach view to presenter
        // and check user state
        mPresenter.attachView(this);
        mPresenter.checkUserState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // detach View to avoid NPE
        mPresenter.detachView();
    }


    @Override
    public void startDeviceListActivity() {
        // go to user home activity (Device List Activity)
        Intent deviceListActivity = new Intent(this, DeviceListActivity.class);
        deviceListActivity.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(deviceListActivity);
        finish();
    }

    @Override
    public void startRegisterActivity() {
        Intent registerActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }

    @Override
    public void startLoginActivity() {
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }
}
