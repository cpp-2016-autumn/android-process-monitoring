package com.appmon.shared.utils.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Handles connection of a {@link ValueEventListener} to a {@link DatabaseReference}
 */

public class FirebaseValueHandler {
    private ValueEventListener mListener;
    private DatabaseReference mReference;

    public FirebaseValueHandler(ValueEventListener listener, DatabaseReference reference){
        mListener = listener;
        mReference = reference;
    }

    /**
     * Connects a {@link ValueEventListener} to a {@link DatabaseReference}
     */
    public void connect(){
        mReference.addValueEventListener(mListener);
    }

    /**
     * Disconnects a {@link ValueEventListener} from a {@link DatabaseReference}
     */
    public void disconnect(){
        mReference.removeEventListener(mListener);
    }
}
