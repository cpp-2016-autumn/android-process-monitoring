package com.appmon.client.bus;

/**
 * A message delivered to a subscriber.
 * @param <T> Type of data this message stores.
 *
 * @see Bus
 */
public class Message<T> {
    /**
     * The contents of this message.
     */
    private T mData;
    /**
     * The topic of this message.
     * Once published anyone interested in this topic will receive this message.
     * @see Topic
     */
    private Topic mTopic;

    /**
     * Creates a message which holds some data on a specific topic.
     * @param data The contents of this message.
     * @param topic The topic of this message.
     */
    public Message(T data, Topic topic) {
        mData = data;
        mTopic = topic;
    }

    /**
     * Public getter for the contents of this message.
     * @return The contents of this message.
     */
    public T getData() {
        return mData;
    }

    /**
     * Public getter for the topic of this message.
     * @return The topic of this message.
     */
    public Topic getTopic() {
        return mTopic;
    }
}
