package com.appmon.control;

import com.appmon.control.models.applist.AppListModel;
import com.appmon.control.models.applist.IAppListModel;
import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
import com.appmon.shared.entities.PackageInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppListModelUnitTest {

    @Mock
    private IDatabaseService mockedDb;

    @Mock
    private IAuthService mockedAuth;

    @Mock
    private ICloudServices mockedCloud;

    @Mock
    private IUser user;

    @Mock
    private IAppListModel.PresenterOps mockedPresenter;

    @Captor
    private ArgumentCaptor<DatabaseChildListener> listener;

    private IAppListModel model;

    @Before
    public void setup() {
        when(user.getUserID()).thenReturn("user");
        when(mockedCloud.getAuth()).thenReturn(mockedAuth);
        when(mockedCloud.getDatabase()).thenReturn(mockedDb);
        model = new AppListModel(mockedCloud);
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
    public void appBlockTest() {
        when(mockedAuth.getUser()).thenReturn(null, user);
        // null user
        model.setAppBlock(mockedPresenter, "test", false);
        verifyNoMoreInteractions(mockedDb);
        model.setAppBlock(mockedPresenter, "test", false);
        verify(mockedDb).setValue(any(String.class), any(Object.class), any(ResultListener.class));
    }
    @Test
    public void actionsTest() {
        when(mockedAuth.getUser()).thenReturn(user);
        model.addPresenter(mockedPresenter);
        verify(mockedPresenter).getDeviceId();
        verify(mockedDb).addChildListener(any(String.class), listener.capture());
        listener.getValue().onChildAdded(mock(IDataSnapshot.class));
        verify(mockedPresenter).onAppAdded(any(String.class), any(PackageInfo.class));
        listener.getValue().onChildRemoved(mock(IDataSnapshot.class));
        verify(mockedPresenter).onAppRemoved(any(String.class));
        listener.getValue().onChildChanged(mock(IDataSnapshot.class));
        verify(mockedPresenter).onAppChanged(any(String.class), any(PackageInfo.class));
        listener.getValue().onCanceled(DatabaseError.NETWORK_ERROR);
        verify(mockedPresenter).onAppListSyncFailed(any(DatabaseError.class));
        model.removePresenter(mockedPresenter);
    }
}
