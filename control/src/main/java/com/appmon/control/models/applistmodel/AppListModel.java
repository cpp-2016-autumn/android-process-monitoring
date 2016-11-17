package com.appmon.control.models.applistmodel;

import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.IUser;
import com.appmon.shared.entities.PackageInfo;

import java.util.HashMap;
import java.util.Map;

public class AppListModel implements IAppListModel {

    private IDatabaseService mDatabase;

    private Map<PresenterOps, DatabaseChildListener> mListenerBridges;

    private String lastUserId = null;

    private IAuthService mAuth;

    public AppListModel(ICloudServices cloudServices) {
        mDatabase = cloudServices.getDatabase();
        mListenerBridges = new HashMap<>();
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
        return userId + "/apps/";
    }


    @Override
    public void addPresenter(final PresenterOps presenter) {
        if (getRootPath() == null)
            return;
        DatabaseChildListener listener = new DatabaseChildListener() {
            @Override
            public void onCanceled(DatabaseError error) {
                // Database event are queued, so we must check if
                // presenter still connected
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
        mDatabase.addChildListener(getRootPath() + presenter.getDeviceId(), listener);
        mListenerBridges.put(presenter, listener);
    }

    @Override
    public void removePresenter(PresenterOps presenter) {
        DatabaseChildListener listener =  mListenerBridges.remove(presenter);
        if (listener != null) {
            mDatabase.removeChildListener(listener);
        }
    }

    @Override
    public void setAppBlock(final PresenterOps presenter, String appId, boolean state) {
        if (getRootPath() == null)
            return;
        // ignore result callback, if node will be changed, it will be passed to presenter
        // using listener
        mDatabase.setValue(getRootPath() + presenter.getDeviceId() + "/" + appId + "/blocked",
                state, null);
    }
}
