package com.appmon.control;

import com.appmon.control.models.applistmodel.IAppListModel;;
import com.appmon.control.presenters.AppListPresenter;
import com.appmon.control.views.IAppListView;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.PackageInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assume.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AppListPresenterUnitTest {

    @Mock
    private IAppListModel mockedModel;
    @Mock
    private IAppListView mockedView;

    @Captor
    ArgumentCaptor<List<PackageInfo>> appList;

    private AppListPresenter presenter;

    @Before
    public void setup() {
        presenter = new AppListPresenter(mockedModel, "test");
        presenter.attachView(mockedView);
    }

    @Test
    public void actions() {
        assertNotNull(presenter.getDeviceId());
        // add element
        presenter.onAppAdded("One", new PackageInfo("App", "com.test", true));
        verify(mockedView).updateList(appList.capture());
        assumeTrue(appList.getValue().size() == 1);
        reset(mockedView);
        // change value
        presenter.onAppChanged("One", new PackageInfo("App", "com.test", false));
        verify(mockedView).updateList(appList.capture());
        assumeTrue(appList.getValue().size() == 1);
        assumeFalse(appList.getValue().get(0).isBlocked());
        reset(mockedView);
        // remove
        presenter.onAppRemoved("One");
        verify(mockedView).updateList(appList.capture());
        assumeTrue(appList.getValue().size() == 0);
        // error
        presenter.onAppListSyncFailed(DatabaseError.NETWORK_ERROR);
        verify(mockedView).showSyncFailMessage();
    }
    @Test
    public void attachDetachTest() {
        AppListPresenter presenter = new AppListPresenter(mockedModel, "test");
        verify(mockedModel, never()).addPresenter(presenter);
        presenter.attachView(mockedView);
        verify(mockedModel).addPresenter(presenter);
        presenter.detachView();
        verify(mockedModel).removePresenter(presenter);
    }

    @Test
    public void updateTest() {
        // presenter must update view
        presenter.requestAppList();
        verify(mockedView).updateList(any(List.class));
    }

    @Test
    public void selectAppTest() {
        presenter.onAppAdded("SomeID", new PackageInfo("App", "com.test", false));
        verify(mockedView).updateList(any(List.class));
        // this index not exists
        presenter.setAppBlockMode(1, false);
        verifyNoMoreInteractions(mockedView);
        presenter.setAppBlockMode(0, true);
        verify(mockedView).updateList(any(List.class));
    }
    @Test
    public void filterTest() {
        presenter.setFilter("test");
        verify(mockedView).updateList(any(List.class));
    }

}
