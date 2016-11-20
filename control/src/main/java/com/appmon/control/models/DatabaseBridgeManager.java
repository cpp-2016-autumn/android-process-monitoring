package com.appmon.control.models;

import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.IDatabaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides listener binding to specific object and removing it using
 * that object.
 * @param <T>
 */
public class DatabaseBridgeManager<T> {

    private IDatabaseService mDatabase;
    private Map<T, DatabaseChildListener> mListenerBridges;

    /**
     * Constructs new database bridge manager
     * @param database reference to underlying database
     */
    public DatabaseBridgeManager(IDatabaseService database) {
        mDatabase = database;
        mListenerBridges = new HashMap<>();
    }

    /**
     * Removes all bound listeners
     */
    public void clearListeners() {
        for (DatabaseChildListener l : mListenerBridges.values()) {
            mDatabase.removeChildListener(l);
        }
    }

    /**
     * Checks if specified object owns ane of registered listeners
     * @param owner object to prfeorm check
     * @return true if object owns listener
     */
    public boolean isObjectRegistered(T owner) {
        return mListenerBridges.containsKey(owner);
    }

    /**
     * Adds new listener and bounds it to it's owner
     * @param path path to listen
     * @param owner future owner of listener
     * @param listener new listener
     */
    public void addListener(String path, T owner, DatabaseChildListener listener) {
        mDatabase.addChildListener(path, listener);
        mListenerBridges.put(owner, listener);
    }

    /**
     * Removes listener and unbinds it from it's owner
     * @param owner object, which listener will be potentially removed
     */
    public void removeListener(T owner) {
        DatabaseChildListener listener = mListenerBridges.remove(owner);
        if (owner != null) {
            mDatabase.removeChildListener(listener);
        }
    }
}
