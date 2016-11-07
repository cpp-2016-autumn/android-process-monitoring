package com.appmon.client.subscribers;

import com.appmon.client.bus.Message;

/**
 * Can be notified of any Messages from topics that it is subcribed to.
 * Created by MikeSotnichek on 11/1/2016.
 */

public interface ISubscriber {
    void notify(Message message);
}
