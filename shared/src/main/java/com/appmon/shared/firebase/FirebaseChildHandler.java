package com.appmon.shared.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

/**
 * Handles connection of a {@link ChildEventListener} to a {@link DatabaseReference}
 */
class FirebaseChildHandler {

    private ChildEventListener mListener;
    private DatabaseReference mReference;

    FirebaseChildHandler(ChildEventListener listener, DatabaseReference reference){
        mListener = listener;
        mReference = reference;
    }

    /**
    * Connects a {@link ChildEventListener} to a {@link DatabaseReference}
    */
    void connect(){
        mReference.addChildEventListener(mListener);
    }

    /**
     * Disconnects a {@link ChildEventListener} from a {@link DatabaseReference}
     */
    void disconnect(){
        mReference.removeEventListener(mListener);
    }
}
