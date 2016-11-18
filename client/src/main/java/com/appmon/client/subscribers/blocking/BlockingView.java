package com.appmon.client.subscribers.blocking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.appmon.client.R;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;

public class BlockingView extends AppCompatActivity {

    private EditText mPasswordField;
    private String mPass;
    private String mPackageToUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mPackageToUnlock = intent.getStringExtra("Unlock package");
        mPass = intent.getStringExtra("Pin");
        setContentView(R.layout.activity_blocking_view);

        mPasswordField = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void cancel(View view){
        BlockingService.getBusInstance().publish(new Message<>(null, Topic.UNBLOCK_APP));
        this.finish();
    }

    public void checkPass(View view){
        if (mPasswordField.getText().toString().equals(mPass)){
            BlockingService.getBusInstance().publish(
                    new Message<>(mPackageToUnlock, Topic.UNBLOCK_APP));
            this.finish();
        } else {
            mPasswordField.setText("");
            mPasswordField.setHint("Wrong password");
        }
    }

}
