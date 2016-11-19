package com.appmon.control.models;

/**
 * Generic Listener Interface for actions, which can fail
 * @param <E> error type
 */
public interface IFallibleListener<E> {
    /**
     * Will be called on success completion of some action
     */
    void onSuccess();

    /**
     * Will be called if some action is failed
     * @param error error, which caused failure
     */
    void onFail(E error);
}
