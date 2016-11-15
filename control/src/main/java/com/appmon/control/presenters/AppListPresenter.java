package com.appmon.control.presenters;

import android.util.SparseArray;

import com.appmon.control.models.applistmodel.IAppListModel;
import com.appmon.control.views.IAppListView;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.PackageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppListPresenter implements IAppListPresenter {

    private IAppListView mView = null;
    private final IAppListModel mModel;
    private final String mDeviceId;

    private Map<String, PackageInfo> mApps;
    private SparseArray<String> mIndexedApps;

    AppListPresenter(IAppListModel model, String deviceId) {
        mModel = model;
        mDeviceId = deviceId;
        mApps = new HashMap<>();
        mIndexedApps = new SparseArray<>();
    }

    private void updateView() {
        mIndexedApps.clear();
        List<PackageInfo> viewList = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, PackageInfo> entry : mApps.entrySet()) {
            viewList.add(entry.getValue());
            mIndexedApps.put(i++, entry.getKey());
        }
        mView.updateList(viewList);
    }

    @Override
    public void requestAppList() {
        updateView();
    }

    @Override
    public void selectApp(int index) {
        String id = mIndexedApps.get(index);
        if (id != null) {
            // change state to opposite
            mModel.setAppBlock(this, id, !mApps.get(id).isBlocked());
        }
    }

    @Override
    public String getDeviceId() {
        return mDeviceId;
    }

    @Override
    public void onAppAdded(String appId, PackageInfo app) {
        mApps.put(appId, app);
        updateView();
    }

    @Override
    public void onAppChanged(String appId, PackageInfo app) {
        mApps.put(appId, app);
        updateView();
    }

    @Override
    public void onAppRemoved(String appId) {
        mApps.remove(appId);
        updateView();
    }

    @Override
    public void onAppListSyncFailed(DatabaseError error) {
        mView.showSyncFailMessage();
    }

    @Override
    public void attachView(IAppListView view) {
        mView = view;
        mModel.addPresenter(this);
    }

    @Override
    public void detachView() {
        mModel.removePresenter(this);
        mView = null;
    }
}
