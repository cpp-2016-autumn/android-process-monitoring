package com.appmon.control.models.devicelist;

import com.appmon.control.models.IBaseModel;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.DeviceInfo;

public interface IDeviceListModel extends IBaseModel {
    interface PresenterOps {
        /**
         * Triggered when new device added
         * @param deviceID new device ID
         * @param device new device info
         */
        void onDeviceAdded(String deviceID, DeviceInfo device);

        /**
         * Triggered when device changed
         * @param deviceID device, which changed
         * @param device new device info
         */
        void onDeviceChanged(String deviceID, DeviceInfo device);

        /**
         * Triggered when device was removed
         * @param deviceID removed device ID
         */
        void onDeviceRemoved(String deviceID);

        /**
         * Triggered when sync error was happened
         * @param error error, which caused failure
         */
        void onDeviceListSyncFailed(DatabaseError error);
    }

    /**
     * Adds new presenter for model
     * @param presenter
     */
    void addPresenter(PresenterOps presenter);

    /**
     * Removes presenter form model and it's all registered callbacks
     * @param presenter
     */
    void removePresenter(PresenterOps presenter);
}
