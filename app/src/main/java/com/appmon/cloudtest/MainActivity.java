package com.appmon.cloudtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView mUsernameText;
    FirebaseAuth mAuth;
    View mNewUserForm;
    View mLogoutForm;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mUsernameText = (TextView) findViewById(R.id.usernameText);
        FirebaseUser user = mAuth.getCurrentUser();
        mNewUserForm = findViewById(R.id.newUserForm);
        mLogoutForm = findViewById(R.id.logoutForm);
        updateUserState(user);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUserState(user);
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void registerBtnClick(View view) {
        Intent intent =  new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void logoutBtnClick(View view) {
        mAuth.signOut();
    }

    private void updateUserState(FirebaseUser user) {
        if (user != null) {
            mUsernameText.setText("user " + user.getEmail() + " logged in");
            mNewUserForm.setVisibility(View.GONE);
            mLogoutForm.setVisibility(View.VISIBLE);
        } else {
            mNewUserForm.setVisibility(View.VISIBLE);
            mLogoutForm.setVisibility(View.GONE);
        }
    }

    public void loginBtnCick(View view) {
        Intent intent =  new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUserState(user);
    }
}
