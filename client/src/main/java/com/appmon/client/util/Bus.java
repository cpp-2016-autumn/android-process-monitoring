package com.appmon.client.com.appmon.client.util;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class for implementing publsher/subscriber pattern.
 *
 * Created by MikeSotnichek on 11/1/2016.
 */

public class Bus {
    private HashMap<String, LinkedList<Subscriber>> topics;
    private Bus instance;
    private Bus(){
        topics = new HashMap<>();
    }

    public Bus getInstance(){
        if (instance == null) instance = new Bus();
        return  instance;
    }

    public void publish(Message message, String topic){
        LinkedList<Subscriber> topicSubscribers = topics.get(topic);
        for (Subscriber sub : topicSubscribers) {
            sub.notify(message);
        }
    }

    public void subscribe(Subscriber subscriber, String topic){
        if (topics.containsKey(topic)){
            topics.get(topic).add(subscriber);
        }else{
            topics.put(topic, new LinkedList<Subscriber>());
            topics.get(topic).add(subscriber);
        }
    }
}
