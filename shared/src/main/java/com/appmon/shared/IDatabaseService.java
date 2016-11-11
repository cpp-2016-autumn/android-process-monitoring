package com.appmon.shared;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Provides access to some database storage
 */
public interface IDatabaseService {

    /**
     * Changes/adds value at specified path. As value accepts:
     * String, Boolean, Long, Double, Map< String, Object >, List< Object >
     * or any Java class, which hva defines default constructor without parameters
     * and public getters for it's values.
     * @param path path to database node
     * @param value value to write
     * @param listener listener, which will handle success or fail events.
     *                 On fail will be passed {@link DatabaseError}. Can be null
     *                 for ignoring all occurred events.
     */
    void setValue(String path, Object value,
                  @Nullable ResultListener<Void, DatabaseError> listener);

    /**
     * Adds new listener to specified location.
     * @param path location to add listener
     * @param listener user-defined data listener.
     * @return listener, passed as parameter
     */
    DatabaseChildListener addChildListener(String path, @NonNull DatabaseChildListener listener);

    /**
     * Unregisters listener from database
     * @param listener listener to remove
     */
    void removeChildListener(DatabaseChildListener listener);

    /**
     * Adds new listener to specified location.
     * @param path location to add listener
     * @param listener user-defined data listener.
     * @return listener, passed as parameter
     */
    DatabaseValueListener addValueListener(String path, @NonNull DatabaseValueListener listener);

    /**
     * Unregisters listener from database
     * @param listener listener to remove
     */
    void removeValueListener(DatabaseValueListener listener);

    /**
     * changes syncing policy ot specified location.
     * @param path path to some subtree
     * @param keepSynced sync flag
     */
    void setKeepSynced(String path, boolean keepSynced);

    /**
     * Enables/disables offline capabilities of Database.
     * (In most cases of our app must be always on from start)
     * @param persistence persistence mode
     */
    void setPersistence(boolean persistence);

    /**
     * Establishes cloud database connection
     */
    void goOnline();

    /**
     * Closes cloud database connection
     */
    void goOffline();
}
