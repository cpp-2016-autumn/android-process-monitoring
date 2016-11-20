package com.appmon.control.models.applist;

import com.appmon.control.models.DatabaseBridgeManager;
import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.entities.PackageInfo;

public class AppListModel implements IAppListModel {

//    private IDatabaseService mDatabaseBridgeManager;
//
//    private Map<PresenterOps, DatabaseChildListener> mListenerBridges;
    private DatabaseBridgeManager<PresenterOps> mDatabaseBridgeManager;
    private IDatabaseService mDatabase;

    private String lastUserId = null;

    private IAuthService mAuth;

    public AppListModel(ICloudServices cloudServices) {
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
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onAppListSyncFailed(error);
                }
            }

            @Override
            public void onChildAdded(IDataSnapshot snapshot) {
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onAppAdded(snapshot.getKey(),
                            snapshot.getValue(PackageInfo.class));
                }
            }

            @Override
            public void onChildChanged(IDataSnapshot snapshot) {
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onAppChanged(snapshot.getKey(),
                            snapshot.getValue(PackageInfo.class));
                }
            }

            @Override
            public void onChildRemoved(IDataSnapshot prevSnapshot) {
                if (mDatabaseBridgeManager.isObjectRegistered(presenter)) {
                    presenter.onAppRemoved(prevSnapshot.getKey());
                }
            }
        };
        mDatabaseBridgeManager.addListener(getRootPath() + presenter.getDeviceId(), presenter,
                listener);
    }

    @Override
    public void removePresenter(PresenterOps presenter) {
        mDatabaseBridgeManager.removeListener(presenter);
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
