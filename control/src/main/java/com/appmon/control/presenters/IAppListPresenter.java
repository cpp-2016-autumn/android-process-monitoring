package com.appmon.control.presenters;

import com.appmon.control.models.applistmodel.IAppListModel;
import com.appmon.control.views.IAppListView;

public interface IAppListPresenter extends IBasePresenter<IAppListView>,
        IAppListModel.PresenterOps {
    void requestAppList();
    void selectApp(int index);
}
