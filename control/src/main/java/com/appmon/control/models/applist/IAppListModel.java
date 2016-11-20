package com.appmon.control.models.applist;

import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.PackageInfo;

public interface IAppListModel {
    interface PresenterOps {
        /**
         * Requested when info about presenter's device is needed
         * for requesting data from database
         * @return device id
         */
        String getDeviceId();

        /**
         * Triggered when new app was added
         * @param appId new app id
         * @param app new app info
         */
        void onAppAdded(String appId, PackageInfo app);

        /**
         * Triggered when app changed
         * @param appId changed app id
         * @param app changed app info
         */
        void onAppChanged(String appId, PackageInfo app);

        /**
         * Triggered when app was removed
         * @param appId application id
         */
        void onAppRemoved(String appId);

        /**
         * Triggered on app list sync error
         * @param error error which caused failure
         */
        void onAppListSyncFailed(DatabaseError error);
    }

    /**
     * Changed state of application block.
     * @param presenter presenter, which requested action
     * @param appId application to modify
     * @param state new block state
     */
    void setAppBlock(PresenterOps presenter, String appId, boolean state);

    /**
     * Adds new presenter
     * @param presenter
     */
    void addPresenter(PresenterOps presenter);

    /**
     * Removes presenter and unregisters all it's listeners
     * @param presenter
     */
    void removePresenter(PresenterOps presenter);
}
