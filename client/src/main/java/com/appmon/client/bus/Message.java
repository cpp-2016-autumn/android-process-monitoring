package com.appmon.client.bus;


/**
 * A message delivered to a subscriber.
 * Created by MikeSotnichek on 11/1/2016.
 */

public class Message<T> {
    private T mData;
    private Topic mTopic;

    public Message(T data, Topic topic) {
        mData = data;
        mTopic = topic;
    }

    public T getData() {
        return mData;
    }

    public Topic getTopic() {
        return mTopic;
    }
}
