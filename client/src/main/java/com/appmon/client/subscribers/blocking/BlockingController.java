package com.appmon.client.subscribers.blocking;

import android.content.Context;

import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.client.subscribers.ISubscriber;

/**
 * Controls the blocking activity.
 * It is an {@link ISubscriber}, subscribed to {@link Topic#PIN_UPDATE} and {@link Topic#BLOCK_APP}.
 */
public class BlockingController implements ISubscriber {
    /**
     * A bus which handles message delivery to this subscriber.
     */
    private Bus mBus;
    /**
     * Current pin code.
     */
    private String mPin;
    /**
     * Context to start the {@link BlockingActivity} in.
     */
    private Context mContext;

    /**
     * A constructor
     * @param bus
     * @param context
     */
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
