package com.appmon.control.views;

import com.appmon.shared.entities.PackageInfo;

import java.util.List;

/**
 * App list view interface
 */
public interface IAppListView extends IBaseView {
    /**
     * Triggered when list was updated
     * @param apps new list
     */
    void updateList(List<PackageInfo> apps);

    /**
     * Triggered when sync error happened
     */
    void showSyncFailMessage();
}
