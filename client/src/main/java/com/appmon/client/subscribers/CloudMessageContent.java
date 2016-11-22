package com.appmon.client.subscribers;

import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;

/**
 * A message delivered to a subscriber to be sent to the cloud
 * Contains information about where the contents will be sent on the cloud.
 */
public class CloudMessageContent {
    /**
     * The path on the cloud.
     */
    private String mPath;
    /**
     * Data to send to the cloud.
     */
    private Object mData;

    /**
     * Creates a message with some contents which should be sent
     *
     * @param path  The path where to place contents.
     * @param value The contents of this message.
     */
    public CloudMessageContent(String path, Object value) {
        mPath = path;
        mData = value;
    }

    /**
     * Public getter for the path.
     *
     * @return The path value.
     */
    public String getPath() {
        return mPath;
    }

    /**
     * Public getter for data.
     *
     * @return The data value.
     */
    public Object getData() {
        return mData;
    }
}
