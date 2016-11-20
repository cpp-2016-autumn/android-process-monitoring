package com.appmon.client.bus;

import com.appmon.client.subscribers.ISubscriber;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Implements publsher/subscriber pattern.
 * Notifies any interested subscribers of published messages on various topics.
 */
public class Bus {
    /**
     * Maps a list of subscribers to a topic.
     */
    private HashMap<Topic, LinkedList<ISubscriber>> mTopics;

    /**
     * Default constructor for {@code Bus}.
     */
    public Bus() {
        mTopics = new HashMap<>();
    }

    /**
     * Publishes a message to its topic.
     * If any listeners are interested, notifies each listener about a message.
     * @param message A message to pass to listeners.
     */
    public void publish(Message message) {
        if (mTopics.containsKey(message.getTopic())) {
            LinkedList<ISubscriber> topicISubscribers = mTopics.get(message.getTopic());
            for (ISubscriber sub : topicISubscribers) {
                sub.notify(message);
            }
        }
    }

    /**
     * Subscribes an {@code ISubscriber} to a specified {@code Topic}.
     * Adds a subscriber to the list of subscribers of the specified topic.
     * Opens a new topic if nessesary.
     * @param subscriber An {@link ISubscriber} to add
     * @param topic A topic which the subscriber is interested in.
     */
    public void subscribe(ISubscriber subscriber, Topic topic) {
        if (mTopics.containsKey(topic)) {
            mTopics.get(topic).add(subscriber);
        } else {
            mTopics.put(topic, new LinkedList<ISubscriber>());
            mTopics.get(topic).add(subscriber);
        }
    }

    /**
     * Unsubscribes an {@code ISubscriber} to a specified {@code Topic}.
     * Removes a subscriber from the list of subscribers of the specified topic.
     * @param subscriber An {@link ISubscriber} to remove
     * @param topic A topic which the subscriber was interested in.
     */
    public void unsubscribe(ISubscriber subscriber, Topic topic) {
        if (mTopics.containsKey(topic)) {
            mTopics.get(topic).remove(subscriber);
        }
    }
}
