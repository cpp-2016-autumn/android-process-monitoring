package com.appmon.shared.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.DatabaseValueListener;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.ResultListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Provides access to a Firebase database.
 * Implements {@link IDatabaseService}
 */

class FirebaseDatabaseService implements IDatabaseService {

    private FirebaseDatabase mDatabase;

    private HashMap<DatabaseChildListener, FirebaseChildHandler> mChildListeners;
    private HashMap<DatabaseValueListener, FirebaseValueHandler> mValueListeners;

    FirebaseDatabaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mChildListeners = new HashMap<>();
        mValueListeners = new HashMap<>();
    }

    @Override
    public void setValue(String path, Object value,
                         @Nullable final ResultListener<Void, DatabaseError> listener) {
        DatabaseReference.CompletionListener firebaseListener = null;
        if (listener != null) {
            firebaseListener = new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(com.google.firebase.database.DatabaseError databaseError,
                                       DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        listener.onFailure(toDatabaseError(databaseError));
                    } else {
                        listener.onSuccess(null);
                    }
                }
            };
        }
        mDatabase.getReference(path).setValue(value, firebaseListener);
    }

    @Override
    public DatabaseChildListener addChildListener(String path,
                                                  @NonNull final DatabaseChildListener listener) {
        ChildEventListener dbListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.onChildAdded(new FirebaseDataSnapshot(dataSnapshot));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listener.onChildChanged(new FirebaseDataSnapshot(dataSnapshot));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.onChildRemoved(new FirebaseDataSnapshot(dataSnapshot));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //unused - ok
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                listener.onCanceled(toDatabaseError(databaseError));
            }
        };
        mChildListeners.put(listener,
                new FirebaseChildHandler(dbListener, mDatabase.getReference(path))).connect();
        return listener;
    }

    @Override
    public void removeChildListener(DatabaseChildListener listener) {
        mChildListeners.get(listener).disconnect();
        mChildListeners.remove(listener);
    }

    @Override
    public DatabaseValueListener addValueListener(String path,
                                                  @NonNull final DatabaseValueListener listener) {
        ValueEventListener dbListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onChanged(new FirebaseDataSnapshot(dataSnapshot));
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                listener.onCanceled(toDatabaseError(databaseError));
            }
        };
        FirebaseValueHandler handler =
                new FirebaseValueHandler(dbListener, mDatabase.getReference(path));
        mValueListeners.put(listener, handler).connect();
        return listener;
    }

    @Override
    public void removeValueListener(DatabaseValueListener listener) {
        mValueListeners.get(listener).disconnect();
        mValueListeners.remove(listener);
    }

    @Override
    public void setKeepSynced(String path, boolean keepSynced) {
        mDatabase.getReference(path).keepSynced(keepSynced);
    }

    @Override
    public void setPersistence(boolean persistence) {
        mDatabase.setPersistenceEnabled(persistence);
    }

    @Override
    public void goOnline() {
        mDatabase.goOnline();
    }

    @Override
    public void goOffline() {
        mDatabase.goOffline();
    }

    private DatabaseError toDatabaseError(com.google.firebase.database.DatabaseError dbError){
        switch (dbError.getCode()) {
            case com.google.firebase.database.DatabaseError.DISCONNECTED:
            case com.google.firebase.database.DatabaseError.MAX_RETRIES:
            case com.google.firebase.database.DatabaseError.NETWORK_ERROR:
                return DatabaseError.NETWORK_ERROR;
            case com.google.firebase.database.DatabaseError.PERMISSION_DENIED:
                return DatabaseError.ACCESS_DENIED;
            default:
                return DatabaseError.INTERNAL_FAIL;
        }
    }

}
