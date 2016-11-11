package com.appmon.shared;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.appmon.shared.utils.firebase.DatabaseChildListener;
import com.appmon.shared.utils.firebase.DatabaseError;
import com.appmon.shared.utils.firebase.DatabaseValueListener;
import com.appmon.shared.utils.firebase.FirebaseChildHandler;
import com.appmon.shared.utils.firebase.FirebaseValueHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Provides access to a Firebase database
 */

public class FirebaseDatabaseService implements IDatabaseService {

    FirebaseDatabase mDatabase;
    HashMap<DatabaseChildListener, FirebaseChildHandler> mChildListeners;
    HashMap<DatabaseValueListener, FirebaseValueHandler> mValueListeners;


    public FirebaseDatabaseService() {
        mDatabase = FirebaseDatabase.getInstance();
        mChildListeners = new HashMap<>();
        mValueListeners = new HashMap<>();
    }

    @Override
    public void setValue(String path, Object value, @Nullable final ResultListener<Void, DatabaseError> listener) {
        mDatabase.getReference(path).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(com.google.firebase.database.DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    switch (databaseError.getCode()) {
                        case com.google.firebase.database.DatabaseError.DISCONNECTED:
                        case com.google.firebase.database.DatabaseError.MAX_RETRIES:
                        case com.google.firebase.database.DatabaseError.NETWORK_ERROR:
                            listener.onFailure(DatabaseError.NETWORK_ERROR);
                            break;
                        case com.google.firebase.database.DatabaseError.PERMISSION_DENIED:
                            listener.onFailure(DatabaseError.ACCESS_DENIED);
                            break;
                        case com.google.firebase.database.DatabaseError.EXPIRED_TOKEN:
                            listener.onFailure(DatabaseError.FRESH_AUTH_NEEDED);
                            break;
                        default:
                            listener.onFailure(DatabaseError.INTERNAL_FAIL);
                            break;
                    }
                }else{
                    listener.onSuccess(null);
                }
            }
        });
    }

    @Override
    public DatabaseChildListener addChildLsitener(String path, final DatabaseChildListener listener) {
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
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                switch (databaseError.getCode()) {
                    case com.google.firebase.database.DatabaseError.DISCONNECTED:
                    case com.google.firebase.database.DatabaseError.MAX_RETRIES:
                    case com.google.firebase.database.DatabaseError.NETWORK_ERROR:
                        listener.onCanceled(DatabaseError.NETWORK_ERROR);
                        break;
                    case com.google.firebase.database.DatabaseError.PERMISSION_DENIED:
                        listener.onCanceled(DatabaseError.ACCESS_DENIED);
                        break;
                    case com.google.firebase.database.DatabaseError.EXPIRED_TOKEN:
                        listener.onCanceled(DatabaseError.FRESH_AUTH_NEEDED);
                        break;
                    default:
                        listener.onCanceled(DatabaseError.INTERNAL_FAIL);
                        break;
                }
            }
        };
        FirebaseChildHandler handler = new FirebaseChildHandler(dbListener, mDatabase.getReference(path));
        mChildListeners.put(listener, handler);
        handler.connect();
        return listener;
    }

    @Override
    public void removeChildListener(DatabaseChildListener listener) {
        mChildListeners.get(listener).disconnect();
        mChildListeners.remove(listener);
    }

    @Override
    public DatabaseValueListener addValueLsitener(String path, final DatabaseValueListener listener) {
        ValueEventListener dbListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onChanged(new FirebaseDataSnapshot(dataSnapshot));
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError databaseError) {
                switch (databaseError.getCode()) {
                    case com.google.firebase.database.DatabaseError.DISCONNECTED:
                    case com.google.firebase.database.DatabaseError.MAX_RETRIES:
                    case com.google.firebase.database.DatabaseError.NETWORK_ERROR:
                        listener.onCanceled(DatabaseError.NETWORK_ERROR);
                        break;
                    case com.google.firebase.database.DatabaseError.PERMISSION_DENIED:
                        listener.onCanceled(DatabaseError.ACCESS_DENIED);
                        break;
                    case com.google.firebase.database.DatabaseError.EXPIRED_TOKEN:
                        listener.onCanceled(DatabaseError.FRESH_AUTH_NEEDED);
                        break;
                    default:
                        listener.onCanceled(DatabaseError.INTERNAL_FAIL);
                        break;
                }
            }
        };
        FirebaseValueHandler handler = new FirebaseValueHandler(dbListener, mDatabase.getReference(path));
        mValueListeners.put(listener, handler);
        handler.connect();
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
}
