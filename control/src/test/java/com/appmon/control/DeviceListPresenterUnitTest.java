package com.appmon.control;

import com.appmon.control.models.devicelist.IDeviceListModel;
import com.appmon.control.presenters.DeviceListPresenter;
import com.appmon.control.views.IDeviceListView;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.DeviceInfo;

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

@RunWith(MockitoJUnitRunner.class)
public class DeviceListPresenterUnitTest {

    @Mock
    private IDeviceListModel mockedModel;
    @Mock
    private IDeviceListView mockedView;

    @Captor
    ArgumentCaptor<List<DeviceInfo>> deviceList;

    private DeviceListPresenter presenter;

    @Before
    public void setup() {
        presenter = new DeviceListPresenter(mockedModel);
        presenter.attachView(mockedView);
    }

    @Test
    public void actions() {
        // add element
        presenter.onDeviceAdded("One", new DeviceInfo("Device"));
        verify(mockedView).updateList(deviceList.capture());
        assumeTrue(deviceList.getValue().size() == 1);
        reset(mockedView);
        // change value
        presenter.onDeviceChanged("One", new DeviceInfo("Kitten"));
        verify(mockedView).updateList(deviceList.capture());
        assumeTrue(deviceList.getValue().size() == 1);
        assumeTrue(deviceList.getValue().get(0).getName().equals("Kitten"));
        reset(mockedView);
        // remove
        presenter.onDeviceRemoved("One");
        verify(mockedView).updateList(deviceList.capture());
        assumeTrue(deviceList.getValue().size() == 0);
        // error
        presenter.onDeviceListSyncFailed(DatabaseError.NETWORK_ERROR);
        verify(mockedView).showSyncFailMessage();
    }
    @Test
    public void attachDetachTest() {
        DeviceListPresenter presenter = new DeviceListPresenter(mockedModel);
        verify(mockedModel, never()).addPresenter(presenter);
        presenter.attachView(mockedView);
        verify(mockedModel).addPresenter(presenter);
        presenter.detachView();
        verify(mockedModel).removePresenter(presenter);
    }

    @Test
    public void updateTest() {
        // presenter must update view
        presenter.requestDeviceList();
        verify(mockedView).updateList(any(List.class));
    }

    @Test
    public void selectDeviceTest() {
        presenter.onDeviceAdded("SomeID", mock(DeviceInfo.class));
        // this index not exists
        presenter.selectDevice(1);
        verify(mockedView, never()).openAppList(any(String.class));
        presenter.selectDevice(0);
        verify(mockedView).openAppList("SomeID");
    }

}
