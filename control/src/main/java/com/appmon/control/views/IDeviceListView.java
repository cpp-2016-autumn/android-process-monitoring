package com.appmon.control.views;

import com.appmon.shared.entities.DeviceInfo;

import java.util.List;

/**
 * Device list view interface
 */
public interface IDeviceListView extends IBaseView {
    /**
     * Triggered when list was updated
     * @param devices new list
     */
    void updateList(List<DeviceInfo> devices);

    /**
     * Triggered when activity must open app list
     * with specified id
     * @param deviceId device to open
     */
    void openAppList(String deviceId);

    /**
     * Triggered when sync error was happened
     */
    void showSyncFailMessage();
}
