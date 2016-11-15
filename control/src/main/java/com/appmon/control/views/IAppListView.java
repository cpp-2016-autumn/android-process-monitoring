package com.appmon.control.views;

import com.appmon.shared.entities.PackageInfo;

import java.util.List;

public interface IAppListView extends IBaseView {
    void updateList(List<PackageInfo> apps);
    void showSyncFailMessage();
}
