package com.appmon.control.models.devicelist;

import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.IUser;
import com.appmon.shared.entities.DeviceInfo;

import java.util.HashMap;
import java.util.Map;

public class DeviceListModel implements IDeviceListModel {

    private final IDatabaseService mDatabase;

    private Map<PresenterOps, DatabaseChildListener> mListenerBridges;

    private String lastUserId = null;

    private IAuthService mAuth;

    public DeviceListModel(ICloudServices cloudServices) {
        mListenerBridges = new HashMap<>();
        mDatabase = cloudServices.getDatabase();
        mAuth = cloudServices.getAuth();
    }

    private void clearListeners() {
        for (DatabaseChildListener l : mListenerBridges.values()) {
            mDatabase.removeChildListener(l);
        }
    }

    private String getRootPath() {
        if (mAuth.getUser() == null)
            return null;
        String userId = mAuth.getUser().getUserID();
        if (!userId.equals(lastUserId)) {
            clearListeners();
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
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onDeviceRemoved(prevSnapshot.getKey());
                }
            }

            @Override
            public void onChildChanged(IDataSnapshot snapshot) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onDeviceChanged(snapshot.getKey(),
                            snapshot.getValue(DeviceInfo.class));
                }
            }

            @Override
            public void onChildAdded(IDataSnapshot snapshot) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onDeviceAdded(snapshot.getKey(),
                            snapshot.getValue(DeviceInfo.class));
                }
            }

            @Override
            public void onCanceled(DatabaseError error) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onDeviceListSyncFailed(error);
                }
            }
        };
        // link listener
        mDatabase.addChildListener(getRootPath(), listener);
        mListenerBridges.put(presenter, listener);

    }

    @Override
    public void removePresenter(PresenterOps presenter) {
        DatabaseChildListener listener =  mListenerBridges.remove(presenter);
        if (listener != null) {
            mDatabase.removeChildListener(listener);
        }
    }
}
