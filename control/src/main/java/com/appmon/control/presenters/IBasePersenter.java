package com.appmon.control.presenters;


import com.appmon.control.views.IBaseView;

public interface IBasePersenter<T extends IBaseView>  {
    void attachView(T view);
    void detachView();
}
