package com.appmon.client.bus;


/**
 * A message delivered to a subscriber.
 * Created by MikeSotnichek on 11/1/2016.
 */

public class Message<T> {
    private T mData;
    private String mTopic;

    public Message(T data, String topic) {
        mData = data;
        mTopic = topic;
    }

    public T getData() {
        return mData;
    }

    public String getTopic() {
        return mTopic;
    }
}
