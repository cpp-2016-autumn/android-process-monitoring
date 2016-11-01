package com.appmon.client.com.appmon.client.util;

/**
 * Can be notified of any Messages from topics that it is subcribed to.
 * Created by MikeSotnichek on 11/1/2016.
 */

public interface Subscriber {
    void notify(Message message);
}
