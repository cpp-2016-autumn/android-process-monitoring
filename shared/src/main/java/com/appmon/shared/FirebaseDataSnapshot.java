package com.appmon.shared;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;

/**
 * Simple {@link com.google.firebase.database.DataSnapshot} wrapper
 * Created by Mike on 11/11/2016.
 */

public class FirebaseDataSnapshot implements IDataSnapshot {

    DataSnapshot mDataSnapshot;

    public FirebaseDataSnapshot(DataSnapshot snapshot){
        mDataSnapshot = snapshot;
    }

    public boolean exists() {
        return mDataSnapshot.exists();
    }

    @Override
    public boolean hasChild(@NonNull String path) {
        return mDataSnapshot.hasChild(path);
    }

    @Override
    public IDataSnapshot child(@NonNull String path) {
        return new FirebaseDataSnapshot(mDataSnapshot.child(path));
    }

    @Override
    public long getChildrenCount() {
        return mDataSnapshot.getChildrenCount();
    }

    @Override
    public Iterable<IDataSnapshot> getChildren() {
        final Iterator<DataSnapshot> iterator = mDataSnapshot.getChildren().iterator();
        return new Iterable<IDataSnapshot>() {
            @Override
            public Iterator<IDataSnapshot> iterator() {
                return new Iterator<IDataSnapshot>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public IDataSnapshot next() {
                        return new FirebaseDataSnapshot(iterator.next());
                    }
                };
            }
        };
    }

    @Override
    public String getKey() {
        return mDataSnapshot.getKey();
    }

    @Override
    public Object getValue() {
        return mDataSnapshot.getValue();
    }

    @Override
    public <T> T getValue(Class<T> valueType) {
        return mDataSnapshot.getValue(valueType);
    }
}
