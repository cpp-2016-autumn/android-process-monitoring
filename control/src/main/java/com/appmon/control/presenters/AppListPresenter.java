package com.appmon.control.presenters;

import com.appmon.control.models.applist.IAppListModel;
import com.appmon.control.views.IAppListView;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.entities.PackageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppListPresenter implements IAppListPresenter {

    private IAppListView mView = null;
    private IAppListModel mModel;
    private String mDeviceId;

    private Map<String, PackageInfo> mApps;
    private Map<Integer, String> mIndexedApps;

    private String mFilter;

    public AppListPresenter(IAppListModel model, String deviceId) {
        mModel = model;
        mDeviceId = deviceId;
        mApps = new HashMap<>();
        mIndexedApps = new HashMap<>();
        mFilter = "";
    }

    /**
     * Generates new app index and app list for displaying in view
     */
    private void updateView() {
        mIndexedApps.clear();
        List<PackageInfo> viewList = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, PackageInfo> entry : mApps.entrySet()) {
            String appName = entry.getValue().getName().toLowerCase();
            if (appName.contains(mFilter)) {
                viewList.add(entry.getValue());
                mIndexedApps.put(i++, entry.getKey());
            }
        }
        mView.updateList(viewList);
    }

    @Override
    public void requestAppList() {
        updateView();
    }

    @Override
    public void setFilter(String filter) {
        mFilter = filter;
        updateView();
    }

    @Override
    public void setAppBlockMode(int index, boolean blocked) {
        String id = mIndexedApps.get(index);
        if (id != null) {
            if (mApps.get(id).isBlocked() != blocked) {
                // change state to opposite
                mModel.setAppBlock(this, id, blocked);
            }
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
