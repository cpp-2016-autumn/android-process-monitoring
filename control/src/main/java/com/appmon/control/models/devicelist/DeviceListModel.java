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
    private final String mDeviceListPath;

    private Map<PresenterOps, DatabaseChildListener> mListenerBridges;

    public DeviceListModel(ICloudServices cloudServices) {
        mListenerBridges = new HashMap<>();
        mDatabase = cloudServices.getDatabase();
        IUser user = cloudServices.getAuth().getUser();
        if (user == null) {
            throw new IllegalStateException("Device list must be created after user sign in check");
        }
        mDeviceListPath = user.getUserID() + "/devices";
    }

    @Override
    public void addPresenter(final PresenterOps presenter) {
        DatabaseChildListener listener =
                mDatabase.addChildListener(mDeviceListPath, new DatabaseChildListener() {
            @Override
            public void onChildRemoved(IDataSnapshot prevSnapshot) {
                presenter.onDeviceRemoved(prevSnapshot.getKey());
            }

            @Override
            public void onChildChanged(IDataSnapshot snapshot) {
                presenter.onDeviceChanged(snapshot.getKey(), snapshot.getValue(DeviceInfo.class));
            }

            @Override
            public void onChildAdded(IDataSnapshot snapshot) {
                presenter.onDeviceAded(snapshot.getKey(), snapshot.getValue(DeviceInfo.class));
            }

            @Override
            public void onCanceled(DatabaseError error) {
                presenter.onDeviceListSyncFailed(error);
            }
        });
        mListenerBridges.put(presenter, listener);

    }

    @Override
    public void removePresenter(PresenterOps presenter) {
        DatabaseChildListener listener =  mListenerBridges.get(presenter);
        if (listener != null) {
            mDatabase.removeChildListener(listener);
        }
    }
}
