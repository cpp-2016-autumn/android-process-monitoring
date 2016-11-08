package com.appmon.client;

/**
 * Tests for publisher/subscriber implementation
 * Created by Mike on 11/6/2016.
 */
import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
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
        bus = Bus.getInstance();
    }


    @Test
    public void testCommunication() {
        //subscribe mock subscribers
        bus.subscribe(mockedSubscriber, "Test topic");
        bus.subscribe(mockedSecondSubscriber, "Test topic");

        bus.subscribe(mockedSecondSubscriber, "Second topic");

        //publish test message to "Test topic"
        Message<String> m = new Message<>("Test message!", "Test topic");
        bus.publish(m);
        verify(mockedSubscriber).notify(m);
        verify(mockedSecondSubscriber).notify(m);

        reset(mockedSubscriber, mockedSecondSubscriber);

        //publish test message to "Second topic"
        Message<String> m1 = new Message<>("Test message!", "Second topic");
        bus.publish(m);
        bus.publish(m1);
        verify(mockedSubscriber, times(1)).notify(m);
        verify(mockedSecondSubscriber, times(2)).notify(any(Message.class));

        reset(mockedSubscriber, mockedSecondSubscriber);

        //publish test message to "No topic"
        Message<String> m2 = new Message<>("Test message!", "No topic");
        bus.publish(m2);
        verifyZeroInteractions(mockedSubscriber, mockedSecondSubscriber);
        reset(mockedSubscriber, mockedSecondSubscriber);
    }

}
