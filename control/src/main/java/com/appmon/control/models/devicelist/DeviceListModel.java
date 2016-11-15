package com.appmon.control.models.devicelist;

import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.IUser;
import com.appmon.shared.entities.DeviceInfo;

import java.util.HashMap;
import java.util.Map;

public class DeviceListModel implements IDeviceListModel {

    private final IDatabaseService mDatabase;
    private final String mRootPath;

    private Map<PresenterOps, DatabaseChildListener> mListenerBridges;

    public DeviceListModel(ICloudServices cloudServices) {
        mListenerBridges = new HashMap<>();
        mDatabase = cloudServices.getDatabase();
        IUser user = cloudServices.getAuth().getUser();
        if (user == null) {
            throw new IllegalStateException("Device list must be created after user sign in check");
        }
        mRootPath = user.getUserID() + "/devices";
    }

    @Override
    public void addPresenter(final PresenterOps presenter) {
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
        mDatabase.addChildListener(mRootPath, listener);
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
