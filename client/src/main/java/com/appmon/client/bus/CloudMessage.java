package com.appmon.client.bus;

/**
 * A message delivered to a subscriber to be sent to the cloud
 * Contains information about where the contents will be sent on the cloud.
 */
public class CloudMessage<T> extends Message<T> {
    /**
     * The path on the cloud.
     */
    private String mPath;

    /**
     * Creates a message with some contents which should be sent
     *
     * @param value The contents of this message.
     * @param path  The path where to place contents.
     * @param topic The topic of this message.
     */
    public CloudMessage(T value, String path, Topic topic) {
        super(value, topic);
        mPath = path;
    }

    /**
     * Public getter for the path.
     *
     * @return The path value.
     */
    public String getPath() {
        return mPath;
    }

}
