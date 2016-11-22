package com.appmon.client;

import com.appmon.client.bus.Bus;
import com.appmon.client.subscribers.CloudMessageContent;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.client.subscribers.CloudManager;
import com.appmon.client.subscribers.ISubscriber;
import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.DatabaseValueListener;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.ResultListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CloudManagerTest {

    private static final String dataPath = "test/path";
    private static final String pinPath = "test/path";

    private ISubscriber cloudManager;
    private Bus bus;
    private Bus busSpy;

    @Mock
    private IDatabaseService mockDb;

    @Captor
    private ArgumentCaptor<DatabaseChildListener> childListener;

    @Captor
    private ArgumentCaptor<DatabaseValueListener> valueListener;
    @Captor
    private ArgumentCaptor<ResultListener<Void, DatabaseError>> writeResultListener;

    @Before
    public void setup() {
        bus = new Bus();
        busSpy = spy(bus);
        cloudManager = new CloudManager(busSpy, mockDb, dataPath, pinPath);
        verify(busSpy).subscribe(any(ISubscriber.class), eq(Topic.WRITE_TO_CLOUD));
        verify(busSpy).subscribe(any(ISubscriber.class), eq(Topic.DELETE_FROM_CLOUD));
        verify(mockDb).addChildListener(any(String.class), childListener.capture());
        verify(mockDb).addValueListener(any(String.class), valueListener.capture());
    }

    @Test
    public void testWrite() {
        //Test write
        bus.publish(new Message<>(new CloudMessageContent(dataPath, "Test"),
                Topic.WRITE_TO_CLOUD));
        verify(mockDb).setValue(eq(dataPath), any(Object.class), writeResultListener.capture());
    }

    @Test
    public void testDelete() {
        //Test delete
        bus.publish(new Message<>(new CloudMessageContent(dataPath, "Test"),
                Topic.DELETE_FROM_CLOUD));
        verify(mockDb).setValue(eq(dataPath), eq(null), writeResultListener.capture());
    }

    @Test
    public void testUpdateChildren() {
        //Test update children
        childListener.getValue().onChildAdded(mock(IDataSnapshot.class));
        verify(busSpy, times(1)).publish(any(Message.class));
    }

    @Test
    public void testUpdatePin() {
        //Test update pin
        valueListener.getValue().onChanged(mock(IDataSnapshot.class));
        verify(busSpy, times(1)).publish(any(Message.class));
    }

}
