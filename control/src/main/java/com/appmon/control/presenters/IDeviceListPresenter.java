package com.appmon.control.presenters;

import com.appmon.control.views.IDeviceListView;

/**
 * Device list presenter interface
 */
public interface IDeviceListPresenter extends IBasePresenter<IDeviceListView> {
    /**
     * request load and update device list in view
     */
    void requestDeviceList();

    /**
     * select device using it's index in list
     * @param index
     */
    void selectDevice(int index);
}
