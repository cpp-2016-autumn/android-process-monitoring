package com.appmon.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appmon.control.persistence.ModelPresenterManager;
import com.appmon.control.presenters.IWelcomePresenter;
import com.appmon.control.views.IWelcomeView;

public class WelcomeActivity extends AppCompatActivity implements IWelcomeView {

    private IWelcomePresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // get presenter and attach this view to it
        mPresenter = ModelPresenterManager.getInstance()
                .getWelcomePresenter();
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
        deviceListActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(deviceListActivity);
        // finish current activity (user can't use "back" button for returning to
        // Welcome Activity
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
