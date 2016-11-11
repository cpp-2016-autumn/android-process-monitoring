package com.appmon.shared.utils.firebase;


import com.appmon.shared.IDataSnapshot;

/**
 * Value listener. It represented with abstract class
 * because this can be useful on cases, when some callbacks
 * must be ignored.
 */
public abstract class DatabaseValueListener {
    /**
     * This method will be triggered when some
     * error were happened when fetching data
     * from database
     * @param error {@link DatabaseError} value which signalizes what caused problem
     */
    public void onCanceled(DatabaseError error) {}

    /**
     *  Will be triggered when value were changed in current location
     * @param snapshot new immutable data snapshot of current location
     */
    public void onChanged(IDataSnapshot snapshot) {}
}
