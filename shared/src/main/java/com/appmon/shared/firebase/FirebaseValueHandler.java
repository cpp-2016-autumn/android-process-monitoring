package com.appmon.shared.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Handles connection of a {@link ValueEventListener} to a {@link DatabaseReference}
 */

class FirebaseValueHandler {
    private ValueEventListener mListener;
    private DatabaseReference mReference;

    FirebaseValueHandler(ValueEventListener listener, DatabaseReference reference){
        mListener = listener;
        mReference = reference;
    }

    /**
     * Connects a {@link ValueEventListener} to a {@link DatabaseReference}
     */
    void connect(){
        mReference.addValueEventListener(mListener);
    }

    /**
     * Disconnects a {@link ValueEventListener} from a {@link DatabaseReference}
     */
    void disconnect(){
        mReference.removeEventListener(mListener);
    }
}
