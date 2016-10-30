package com.appmon.control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // setup listeners
        Button loginButton = (Button) findViewById(R.id.welcomeLoginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(WelcomeActivity.this, DeviceListActivity.class);
                startActivity(loginActivity);
            }
        });
        Button registerButton = (Button) findViewById(R.id.welcomeRegisterBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
        // if logged in = > Open DeviceListActivity

    }
}
