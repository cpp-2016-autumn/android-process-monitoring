package com.appmon.client.subscribers;

import com.appmon.client.bus.Message;

/**
 * Listens to messages on some topics.
 * Can be notified of any messages from topics that it is subcribed to.
 *
 * @see com.appmon.client.bus.Bus
 */
public interface ISubscriber {

    /**
     * Receive and handle a message.
     *
     * @param message A message to handle.
     */
    void notify(Message message);

    /**
     * Unsubscribe from any subscribed topics.
     */
    void cleanUp();
}
