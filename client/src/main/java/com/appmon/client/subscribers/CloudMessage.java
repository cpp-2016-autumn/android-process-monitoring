package com.appmon.client.subscribers;

import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;

/**
 * Created by Mike on 11/16/2016.
 */

public class CloudMessage<T> extends Message<T> {
    private String path;

    public CloudMessage(T value, String path, Topic topic){
        super(value, topic);
        this.path = path;
    }

    public String getPath(){
        return  path;
    }

}
