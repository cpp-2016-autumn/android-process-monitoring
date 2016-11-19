package com.appmon.control.presenters;

import com.appmon.control.models.applist.IAppListModel;
import com.appmon.control.views.IAppListView;

/**
 * Application list presenter interface
 */
public interface IAppListPresenter extends IBasePresenter<IAppListView>,
        IAppListModel.PresenterOps {
    /**
     * request load and update application list in view
     */
    void requestAppList();

    /**
     * Change application block state
     * @param index index of application in presented list
     * @param blocked new state of application block
     */
    void setAppBlockMode(int index, boolean blocked);

    /**
     * Change filter text, which used to filter application list
     * (search trough applications)
     * @param filter new filter text
     */
    void setFilter(String filter);
}
