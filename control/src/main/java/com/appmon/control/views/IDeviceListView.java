package com.appmon.control.views;

import com.appmon.shared.entities.DeviceInfo;

import java.util.List;

public interface IDeviceListView extends IBaseView {
    void updateList(List<DeviceInfo> devices);
    void openAppList(String deviceId);
    void showSyncFailMessage();
}
