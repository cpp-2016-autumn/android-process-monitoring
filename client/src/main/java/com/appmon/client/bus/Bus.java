package com.appmon.client.bus;

import com.appmon.client.subscribers.ISubscriber;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class for implementing publsher/subscriber pattern.
 * Created by MikeSotnichek on 11/1/2016.
 */

public class Bus {
    private HashMap<String, LinkedList<ISubscriber>> mTopics;
    private static Bus Instance = null;

    private Bus() {
        mTopics = new HashMap<>();
    }

    public static Bus getInstance() {
        if (Instance == null) Instance = new Bus();
        return Instance;
    }

    public void publish(Message message) {
        if (mTopics.containsKey(message.getTopic())) {
            LinkedList<ISubscriber> topicISubscribers = mTopics.get(message.getTopic());
            for (ISubscriber sub : topicISubscribers) {
                sub.notify(message);
            }
        }
    }

    public void subscribe(ISubscriber ISubscriber, String topic) {
        if (mTopics.containsKey(topic)) {
            mTopics.get(topic).add(ISubscriber);
        } else {
            mTopics.put(topic, new LinkedList<ISubscriber>());
            mTopics.get(topic).add(ISubscriber);
        }
    }

    public void unSubscribe(ISubscriber ISubscriber, String topic) {
        if (mTopics.containsKey(topic)) {
            mTopics.get(topic).remove(ISubscriber);
        }
    }
}
