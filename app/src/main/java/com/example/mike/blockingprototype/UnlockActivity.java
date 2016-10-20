package com.example.mike.blockingprototype;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UnlockActivity extends AppCompatActivity {
    EditText password;
    private static String pass = "test123";
    private String unlockPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        unlockPackage = intent.getStringExtra("Unlock package");
        setContentView(R.layout.activity_unlock);

        password = (EditText) findViewById(R.id.passTxt);
    }

    @Override
    public void onBackPressed() {
        Intent unlockIntent = new Intent(this, BlockingService.class);
        unlockIntent.putExtra("Unlock action", 2);
        startService(unlockIntent);

        this.finish();
    }

    public void cancel(View view){
        Intent unlockIntent = new Intent(this, BlockingService.class);
        unlockIntent.putExtra("Unlock action", 2);
        startService(unlockIntent);

        this.finish();
    }

    public void checkPass(View view){
        if (password.getText().toString().equals(pass)){
            Intent unlockIntent = new Intent(this, BlockingService.class);
            unlockIntent.putExtra("Unlock action", 1);
            unlockIntent.putExtra("Unlock package", unlockPackage);
            startService(unlockIntent);

            unlockIntent = new Intent(unlockPackage);
            unlockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startService(unlockIntent);

            this.finish();
        } else {
            password.setText("");
            password.setHint("Wrong password");
        }
    }
}
