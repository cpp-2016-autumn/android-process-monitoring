package com.appmon.control.presenters;

import android.util.SparseArray;

import com.appmon.control.models.devicelist.IDeviceListModel;
import com.appmon.control.views.IDeviceListView;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.DeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceListPresenter implements IDeviceListPresenter, IDeviceListModel.PresenterOps {

    private IDeviceListView mView = null;
    private IDeviceListModel mModel;

    // DeviceId => DeviceInfo
    private Map<String, DeviceInfo> mDevices;
    // Generated View List Item Position => DeviceInfo
    private Map<Integer, String> mIndexedDevices;

    public DeviceListPresenter(IDeviceListModel model) {
        mModel = model;
        mDevices = new HashMap<>();
        mIndexedDevices = new HashMap<>();
    }


    private void updateView() {
        mIndexedDevices.clear();
        List<DeviceInfo> viewList = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, DeviceInfo> entry : mDevices.entrySet()) {
            viewList.add(entry.getValue());
            mIndexedDevices.put(i++, entry.getKey());
        }
        mView.updateList(viewList);
    }

    @Override
    public void requestDeviceList() {
        // force generate list and pass it to view
        updateView();
    }

    @Override
    public void selectDevice(int index) {
        // send request to open AppList Activity
        String id = mIndexedDevices.get(index);
        if (id != null) {
            mView.openAppList(id);
        }
    }

    @Override
    public void attachView(IDeviceListView view) {

        mView = view;
        mModel.addPresenter(this);
    }

    @Override
    public void detachView() {
        mModel.removePresenter(this);
        mView = null;
    }

    @Override
    public void onDeviceAdded(String deviceID, DeviceInfo device) {
        mDevices.put(deviceID, device);
        updateView();
    }

    @Override
    public void onDeviceChanged(String deviceID, DeviceInfo device) {
        mDevices.put(deviceID, device);
        updateView();
    }

    @Override
    public void onDeviceRemoved(String deviceID) {
        mDevices.remove(deviceID);
        updateView();
    }

    @Override
    public void onDeviceListSyncFailed(DatabaseError error) {
        mView.showSyncFailMessage();
    }
}
