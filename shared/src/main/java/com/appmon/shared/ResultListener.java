package com.appmon.shared;

/**
 * Generic async result listener.
 * Made as abstract class for possibility to
 * ignore one of handlers.
 * @param <V> Success value type
 * @param <E> Error value type
 */
public abstract class ResultListener<V, E> {
    public void onSuccess(V value) {}
    public void onFailure(E error) {}
}
