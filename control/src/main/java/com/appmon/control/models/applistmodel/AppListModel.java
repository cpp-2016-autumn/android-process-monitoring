package com.appmon.control.models.applistmodel;

import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.IUser;
import com.appmon.shared.entities.PackageInfo;

import java.util.HashMap;
import java.util.Map;

public class AppListModel implements IAppListModel {

    final IDatabaseService mDatabase;
    final String mRootPath;

    Map<PresenterOps, DatabaseChildListener> mListenerBridges;

    public AppListModel(ICloudServices cloudServices) {
        mDatabase = cloudServices.getDatabase();
        mListenerBridges = new HashMap<>();
        IUser user = cloudServices.getAuth().getUser();
        if (user == null) {
            throw new IllegalStateException("AppList must be created after user sign in check");
        }
        mRootPath = user.getUserID() + "/apps/";
    }

    @Override
    public void setAppBlock(final PresenterOps presenter, String appId, boolean state) {
        // ignore result callback, if node will be changed, it will be passed to presenter
        // using listener
        mDatabase.setValue(mRootPath + presenter.getDeviceId() + "/" + appId + "/blocked",
                state, null);
    }

    @Override
    public void addPresenter(final PresenterOps presenter) {
        DatabaseChildListener listener = new DatabaseChildListener() {
            @Override
            public void onCanceled(DatabaseError error) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onAppListSyncFailed(error);
                }
            }

            @Override
            public void onChildAdded(IDataSnapshot snapshot) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onAppAdded(snapshot.getKey(),
                            snapshot.getValue(PackageInfo.class));
                }
            }

            @Override
            public void onChildChanged(IDataSnapshot snapshot) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onAppChanged(snapshot.getKey(),
                            snapshot.getValue(PackageInfo.class));
                }
            }

            @Override
            public void onChildRemoved(IDataSnapshot prevSnapshot) {
                if (mListenerBridges.containsKey(presenter)) {
                    presenter.onAppRemoved(prevSnapshot.getKey());
                }
            }
        };
        mDatabase.addChildListener(mRootPath + presenter.getDeviceId(), listener);
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
