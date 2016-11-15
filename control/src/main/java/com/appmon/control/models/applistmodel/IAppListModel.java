package com.appmon.control.models.applistmodel;

import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.PackageInfo;

public interface IAppListModel {
    interface PresenterOps {
        String getDeviceId();
        void onAppAdded(String appId, PackageInfo app);
        void onAppChanged(String appId, PackageInfo app);
        void onAppRemoved(String appId);
        void onAppListSyncFailed(DatabaseError error);
    }
    void setAppBlock(PresenterOps presenter, String appId, boolean state);
    void addPresenter(PresenterOps presenter);
    void removePresenter(PresenterOps presenter);
}
