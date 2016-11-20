package com.appmon.client.bus;

import com.appmon.client.subscribers.ISubscriber;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class for implementing publsher/subscriber pattern.
 * Notifies any interested subscribers of published messages on various topics.
 */

public class Bus {


    private HashMap<Topic, LinkedList<ISubscriber>> mTopics;

    public Bus() {
        mTopics = new HashMap<>();
    }

    public void publish(Message message) {
        if (mTopics.containsKey(message.getTopic())) {
            LinkedList<ISubscriber> topicISubscribers = mTopics.get(message.getTopic());
            for (ISubscriber sub : topicISubscribers) {
                sub.notify(message);
            }
        }
    }

    public void subscribe(ISubscriber ISubscriber, Topic topic) {
        if (mTopics.containsKey(topic)) {
            mTopics.get(topic).add(ISubscriber);
        } else {
            mTopics.put(topic, new LinkedList<ISubscriber>());
            mTopics.get(topic).add(ISubscriber);
        }
    }

    public void unsubscribe(ISubscriber ISubscriber, Topic topic) {
        if (mTopics.containsKey(topic)) {
            mTopics.get(topic).remove(ISubscriber);
        }
    }
}
