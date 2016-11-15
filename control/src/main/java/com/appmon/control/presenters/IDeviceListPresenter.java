package com.appmon.control.presenters;

import com.appmon.control.views.IDeviceListView;

public interface IDeviceListPresenter extends IBasePresenter<IDeviceListView> {
    void requestDeviceList();
    void selectDevice(int index);
}
