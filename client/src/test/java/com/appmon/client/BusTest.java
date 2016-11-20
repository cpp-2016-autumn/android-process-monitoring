package com.appmon.client;

/**
 * Tests for publisher/subscriber implementation
 * Created by Mike on 11/6/2016.
 */
import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.client.subscribers.ISubscriber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BusTest {

    @Mock
    private ISubscriber mockedSubscriber;
    @Mock
    private ISubscriber mockedSecondSubscriber;

    private Bus bus;

    @Before
    public void setup() {
        bus = new Bus();
    }


    @Test
    public void testCommunication() {
        //subscribe mock subscribers
        bus.subscribe(mockedSubscriber, Topic.BLOCK_APP);
        bus.subscribe(mockedSecondSubscriber, Topic.BLOCK_APP);

        bus.subscribe(mockedSecondSubscriber, Topic.BLOCK_APP);

        //publish test message to "Test topic"
        Message<String> m = new Message<>("Test message!", Topic.BLOCK_APP);
        bus.publish(m);
        verify(mockedSubscriber).notify(m);
        verify(mockedSecondSubscriber).notify(m);

        reset(mockedSubscriber, mockedSecondSubscriber);

        //publish test message to "Second topic"
        Message<String> m1 = new Message<>("Test message!", Topic.BLOCK_APP);
        bus.publish(m);
        bus.publish(m1);
        verify(mockedSubscriber, times(1)).notify(m);
        verify(mockedSecondSubscriber, times(2)).notify(any(Message.class));

        reset(mockedSubscriber, mockedSecondSubscriber);

        //publish test message to "No topic"
        Message<String> m2 = new Message<>("Test message!", Topic.WRITE_TO_CLOUD);
        bus.publish(m2);
        verifyZeroInteractions(mockedSubscriber, mockedSecondSubscriber);
        reset(mockedSubscriber, mockedSecondSubscriber);
    }

}
