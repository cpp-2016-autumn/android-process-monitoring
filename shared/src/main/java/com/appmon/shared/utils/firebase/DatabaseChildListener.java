package com.appmon.shared.utils.firebase;


import com.appmon.shared.IDataSnapshot;

/**
 * Child listener. It represented with abstract class
 * because this can be useful on cases, when some callbacks
 * must be ignored
 */
public abstract class DatabaseChildListener {
    /**
     * This method will be triggered when some
     * error were happened when fetching data
     * from database
     * @param error {@link DatabaseError} value which signalizes what caused problem
     */
    public void onCanceled(DatabaseError error) {}

    /**
     *  Will be triggered when some child were added to current location
     * @param snapshot immutable data snapshot of new child location
     */
    public void onChildAdded(IDataSnapshot snapshot) {}

    /**
     *  Will be triggered when some child were changed in current location
     * @param snapshot new immutable data snapshot of child location
     */
    public void onChildChanged(IDataSnapshot snapshot) {}

    /**
     * Will be triggered when some child were removed from current location
     * @param prevSnapshot last immutable snapshot of removed child location
     */
    public void onChildRemoved(IDataSnapshot prevSnapshot) {}
}
