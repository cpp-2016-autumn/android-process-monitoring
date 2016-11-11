package com.appmon.shared.utils.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

/**
 * Handles connection of a {@link ChildEventListener} to a {@link DatabaseReference}
 */

public class FirebaseChildHandler {
    private ChildEventListener mListener;
    private DatabaseReference mReference;

    public FirebaseChildHandler(ChildEventListener listener, DatabaseReference reference){
        mListener = listener;
        mReference = reference;
    }

    /**
    * Connects a {@link ChildEventListener} to a {@link DatabaseReference}
    */
    public void connect(){
        mReference.addChildEventListener(mListener);
    }

    /**
     * Disconnects a {@link ChildEventListener} from a {@link DatabaseReference}
     */
    public void disconnect(){
        mReference.removeEventListener(mListener);
    }
}
