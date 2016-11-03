package com.appmon.control.models;

/**
 * Created by pacmancoder on 03.11.16.
 */

public interface IFallibleListener<E> {
    void onSuccess();
    void onFail(E error);
}
