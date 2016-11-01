package com.appmon.client.com.appmon.client.util;


/**
 * A message delivered to a subscriber.
 * Created by MikeSotnichek on 11/1/2016.
 */

public class Message {
    private Object data;
    public Message(Object data){
        this.data = data;
    }
    public Object getData(){
        return data;
    }
}
