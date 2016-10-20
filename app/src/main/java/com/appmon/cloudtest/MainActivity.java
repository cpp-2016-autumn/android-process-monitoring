package com.appmon.cloudtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView mUsernameText;
    View mNewUserForm;
    View mLogoutForm;
    Button mTaskBtn;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameText = (TextView) findViewById(R.id.usernameText);
        mNewUserForm = findViewById(R.id.newUserForm);
        mLogoutForm = findViewById(R.id.logoutForm);
        mTaskBtn = (Button) findViewById(R.id.taskActivityBtn);
        // init auth-related things
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUserState(user);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUserState(user);
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        // Open user task activity list
        mTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUserState(user);
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

    public void loginBtnCick(View view) {
        Intent intent =  new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void logoutBtnClick(View view) {
        mAuth.signOut();
    }

    /// Updates login/register UI
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
}
