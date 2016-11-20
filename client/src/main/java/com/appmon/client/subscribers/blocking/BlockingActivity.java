package com.appmon.client.subscribers.blocking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.appmon.client.R;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;

public class BlockingActivity extends AppCompatActivity {

    private EditText mPasswordField;
    private String mPass;
    private String mPackageToUnlock;

    public static void startActivity(Context context, String packageName, String pin){
        Intent intent = new Intent(context.getApplicationContext(), BlockingActivity.class);
        intent.putExtra("Unlock package", packageName);
        intent.putExtra("Pin", pin);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mPackageToUnlock = intent.getStringExtra("Unlock package");
        mPass = intent.getStringExtra("Pin");
        setContentView(R.layout.activity_blocking);

        mPasswordField = (EditText) findViewById(R.id.password);
        mPasswordField.addTextChangedListener(passwordWatcher);
    }

    @Override
    public void onBackPressed() {
        BlockingService.getBusInstance().publish(new Message<>(null, Topic.UNBLOCK_APP));
        this.finish();
    }

    public void cancel(View view) {
        onBackPressed();
    }

    public void checkPass(View view) {
        if (mPasswordField.getText().toString().equals(mPass)) {
            BlockingService.getBusInstance().publish(
                    new Message<>(mPackageToUnlock, Topic.UNBLOCK_APP));
            this.finish();
        } else {
            mPasswordField.setText("");
            mPasswordField.setHint("Wrong password");
        }
    }


    private TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //OK
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //OK
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mPasswordField.getText().toString().equals(mPass)) {
                BlockingService.getBusInstance().publish(
                        new Message<>(mPackageToUnlock, Topic.UNBLOCK_APP));
                BlockingActivity.this.finish();
            }
        }
    };
}
