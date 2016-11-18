package com.appmon.client.subscribers.blocking;

import android.content.Context;
import android.content.Intent;

import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.client.subscribers.ISubscriber;
import com.appmon.shared.entities.PackageInfo;

/**
 * Created by Mike on 11/18/2016.
 */

public class BlockingScreenManager implements ISubscriber {
    private Bus mBus;
    private String mPin;
    private Context mContext;

    public BlockingScreenManager(Bus bus, Context context) {
        mBus = bus;
        mBus.subscribe(this, Topic.PIN_UPDATE);
        mBus.subscribe(this, Topic.BLOCK_APP);
        mContext = context;
    }

    @Override
    public void notify(Message message) {
        switch (message.getTopic()) {
            case PIN_UPDATE:
                mPin = message.getData().toString();
                break;
            case BLOCK_APP:
                Intent intent = new Intent(mContext.getApplicationContext(), BlockingView.class);
                intent.putExtra("Unlock package", message.getData().toString());
                intent.putExtra("Pin", mPin);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                mContext.getApplicationContext().startActivity(intent);
                break;
        }
    }

    @Override
    public void cleanUp() {
        mBus.unsubscribe(this, Topic.PIN_UPDATE);
        mBus.unsubscribe(this, Topic.BLOCK_APP);
    }
}
