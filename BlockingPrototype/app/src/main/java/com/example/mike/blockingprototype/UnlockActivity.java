package com.example.mike.blockingprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UnlockActivity extends AppCompatActivity {
    EditText password;
    boolean unlock = false;
    private static String pass = "test123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        password = (EditText) findViewById(R.id.passTxt);
    }
    public void cancel(View view){
        unlock = false;
    }

    public void checkPass(View view){
        unlock = false;
        if (password.getText().toString() == pass){
            unlock = true;
            stopSe
        }
    }
}
