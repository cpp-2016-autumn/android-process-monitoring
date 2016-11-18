package com.appmon.control.presenters;

import com.appmon.control.views.IBaseView;

/**
 * Base interface for all presenters. Requires that all presenters
 * must handle single View.
 * @param <T> View interface of presenter.
 */
public interface IBasePresenter<T extends IBaseView>  {
    /**
     * Attaches single view to current presenter
     * @param view
     */
    void attachView(T view);

    /**
     * Detaches single view from current presenter
     */
    void detachView();
}
