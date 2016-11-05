package com.appmon.control.models;

/**
 * Generic Listener Interface for actions, which can fail
 * @param <E> error type
 */
public interface IFallibleListener<E> {
    void onSuccess();
    void onFail(E error);
}
