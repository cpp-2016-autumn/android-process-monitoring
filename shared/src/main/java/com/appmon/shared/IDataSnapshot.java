package com.appmon.shared;

import android.support.annotation.NonNull;

/**
 * Immutable data snapshot interface
 */
public interface IDataSnapshot {
    /**
     * Checks if current node exists
     * @return true it exists
     */
    boolean exists();

    /**
     * Checks if child at given relative path exists
     * @param path relative path
     * @return true if child exists
     */
    boolean hasChild(@NonNull String path);

    /**
     * Returns snapshot of child located at given relative path
     * @param path relative path
     * @return immutable child snapshot
     */
    IDataSnapshot child(@NonNull String path);

    /**
     * Returns current node child count
     * @return child count
     */
    long getChildrenCount();

    /**
     * Returns iterable over all node children
     * @return iterable of IDataSnapshot
     */
    Iterable<IDataSnapshot> getChildren();

    /**
     * Returns key of current node
     * @return
     */
    String getKey();

    /**
     * Returns object of native class:
     * String, Bool, Long, Double, Map< String, Object >, List<Object>
     * @return
     */
    Object getValue();

    /**
     * Returns value using marshaling to class of your choose.
     * Custom class must have default constructor and public setters
     * for its values.
     * @param valueType class, to which value must be marshaled
     * @param <T> generic parameter
     * @return marshaled value
     */
    <T>T getValue(Class<T> valueType);
}
