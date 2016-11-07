package com.appmon.control.presenters;

import com.appmon.control.views.IBaseView;

/**
 * Base interface for all presenters. Requires that all presenters
 * must handle single View.
 * @param <T> View interface of presenter.
 */
public interface IBasePresenter<T extends IBaseView>  {
    void attachView(T view);
    void detachView();
}
