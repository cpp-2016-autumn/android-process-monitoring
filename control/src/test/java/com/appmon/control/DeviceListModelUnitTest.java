package com.appmon.control;

import com.appmon.control.models.devicelist.DeviceListModel;
import com.appmon.control.models.devicelist.IDeviceListModel;
import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.IUser;
import com.appmon.shared.entities.DeviceInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeviceListModelUnitTest {

    @Mock
    private IDatabaseService mockedDb;

    @Mock
    private IAuthService mockedAuth;

    @Mock
    private ICloudServices mockedCloud;

    @Mock
    private IUser user;

    @Mock
    private IDeviceListModel.PresenterOps mockedPresenter;

    @Captor
    private ArgumentCaptor<DatabaseChildListener> listener;

    private IDeviceListModel model;

    @Before
    public void setup() {
        when(user.getUserID()).thenReturn("user");
        when(mockedCloud.getAuth()).thenReturn(mockedAuth);
        when(mockedCloud.getDatabase()).thenReturn(mockedDb);
        model = new DeviceListModel(mockedCloud);
    }

    @Test
    public void userCheckTest() {
        IUser secondUser = mock(IUser.class);
        when(secondUser.getUserID()).thenReturn("second");
        when(mockedAuth.getUser()).thenReturn(null, user, secondUser);
        model.addPresenter(mockedPresenter);
        // do not add any listeners
        verifyNoMoreInteractions(mockedDb);
        model.addPresenter(mockedPresenter);
        model.addPresenter(mockedPresenter);
    }

    @Test
    public void actionsTest() {
        when(mockedAuth.getUser()).thenReturn(user);
        model.addPresenter(mockedPresenter);
        verify(mockedDb).addChildListener(any(String.class), listener.capture());
        listener.getValue().onChildAdded(mock(IDataSnapshot.class));
        verify(mockedPresenter).onDeviceAdded(any(String.class), any(DeviceInfo.class));
        listener.getValue().onChildRemoved(mock(IDataSnapshot.class));
        verify(mockedPresenter).onDeviceRemoved(any(String.class));
        listener.getValue().onChildChanged(mock(IDataSnapshot.class));
        verify(mockedPresenter).onDeviceChanged(any(String.class), any(DeviceInfo.class));
        listener.getValue().onCanceled(DatabaseError.NETWORK_ERROR);
        verify(mockedPresenter).onDeviceListSyncFailed(any(DatabaseError.class));
        model.removePresenter(mockedPresenter);
    }
}
