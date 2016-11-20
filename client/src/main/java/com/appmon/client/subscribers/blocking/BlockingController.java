package com.appmon.client.subscribers.blocking;

import android.content.Context;
import android.content.Intent;

import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.client.subscribers.ISubscriber;

/**
 * Created by Mike on 11/18/2016.
 */

public class BlockingController implements ISubscriber {
    private Bus mBus;
    private String mPin;
    private Context mContext;

    public BlockingController(Bus bus, Context context) {
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
                BlockingActivity.startActivity(mContext, message.getData().toString(), mPin);
                break;
        }
    }

    @Override
    public void cleanUp() {
        mBus.unsubscribe(this, Topic.PIN_UPDATE);
        mBus.unsubscribe(this, Topic.BLOCK_APP);
    }
}
