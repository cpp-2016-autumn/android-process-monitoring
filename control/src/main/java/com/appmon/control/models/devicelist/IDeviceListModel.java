package com.appmon.control.models.devicelist;

import com.appmon.control.models.IBaseModel;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.DeviceInfo;

public interface IDeviceListModel extends IBaseModel {
    interface PresenterOps {
        void onDeviceAdded(String deviceID, DeviceInfo device);
        void onDeviceChanged(String deviceID, DeviceInfo device);
        void onDeviceRemoved(String deviceID);
        void onDeviceListSyncFailed(DatabaseError error);
    }

    void addPresenter(PresenterOps presenter);
    void removePresenter(PresenterOps presenter);
}
