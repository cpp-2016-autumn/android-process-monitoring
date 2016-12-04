package com.appmon.control.models.devicelist;

import com.appmon.control.models.DatabaseBridgeManager;
import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.entities.DeviceInfo;

public class DeviceListModel implements IDeviceListModel {

    private DatabaseBridgeManager<PresenterOps> mDatabaseBridgeManager;

    private String lastUserId = null;

    private IAuthService mAuth;
    private IDatabaseService mDatabase;
    public DeviceListModel(ICloudServices cloudServices) {
        mDatabase = cloudServices.getDatabase();
        mDatabaseBridgeManager = new DatabaseBridgeManager<>(mDatabase);
        mAuth = cloudServices.getAuth();
    }

    private String getRootPath() {
        if (mAuth.getUser() == null)
            return null;
        String userId = mAuth.getUser().getUserID();
        if (!userId.equals(lastUserId)) {
            mDatabaseBridgeManager.clearListeners();
            lastUserId = userId;
        }
        return userId + "/devices";
    }

    @Override
    public void addPresenter(final PresenterOps presenter) {
        if (getRootPath() == null) {
            return;
        }
        DatabaseChildListener listener = new DatabaseChildListener() {
            @Override
            public void onChildRemoved(IDataSnapshot prevSnapshot) {
                // Database event are queued, so we must check if
                // presenter still connected
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onDeviceRemoved(prevSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(IDataSnapshot snapshot) {
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onDeviceChanged(snapshot.getKey(),
                            snapshot.getValue(DeviceInfo.class));
                }
            }

            @Override
            public void onChildAdded(IDataSnapshot snapshot) {
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onDeviceAdded(snapshot.getKey(),
                            snapshot.getValue(DeviceInfo.class));
                }
            }

            @Override
            public void onCanceled(DatabaseError error) {
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onDeviceListSyncFailed(error);
                }
            }
        };
        // link listener
        mDatabaseBridgeManager.addListener(getRootPath(), presenter, listener);
        mDatabase.goOnline();
    }

    @Override
    public void removePresenter(PresenterOps presenter) {
        mDatabase.goOffline();
        mDatabaseBridgeManager.removeListener(presenter);
    }
}
