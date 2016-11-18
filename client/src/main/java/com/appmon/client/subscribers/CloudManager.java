package com.appmon.client.subscribers;

import android.app.Notification;
import android.util.Log;

import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.shared.DatabaseChildListener;
import com.appmon.shared.DatabaseError;
import com.appmon.shared.DatabaseValueListener;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.ResultListener;
import com.appmon.shared.entities.PackageInfo;
import com.appmon.shared.firebase.FirebaseCloudServices;

public class CloudManager implements ISubscriber {

    private Bus mBus;
    private IDatabaseService mDbService;
    private DatabaseChildListener mAppInfoListener;
    private DatabaseValueListener mPinListener;

    public CloudManager(Bus bus, String appInfoPath, String pinPath) {
        mBus = bus;
        mBus.subscribe(this, Topic.WRITE_TO_CLOUD);
        mBus.subscribe(this, Topic.DELETE_FROM_CLOUD);

        mAppInfoListener = new DatabaseChildListener() {
            @Override
            public void onChildAdded(IDataSnapshot snapshot) {
                PackageInfo info = snapshot.getValue(PackageInfo.class);
                Message<PackageInfo> message = new Message<>(info, Topic.APP_STATE_UPDATE);
                Log.d("CLOUD", "Message :" + info.getPackageName());
                mBus.publish(message);
            }
            @Override
            public void onChildChanged(IDataSnapshot snapshot) {
                PackageInfo info = snapshot.getValue(PackageInfo.class);
                Message<PackageInfo> message = new Message<>(info, Topic.APP_STATE_UPDATE);
                Log.d("CLOUD", "Message :" + info.getPackageName());
                mBus.publish(message);
            }
        };
        mPinListener = new DatabaseValueListener() {
            @Override
            public void onChanged(IDataSnapshot snapshot) {
                Message<String> message = new Message<>(snapshot.getValue().toString(), Topic.PIN_UPDATE);
                mBus.publish(message);
            }
        };

        mDbService = FirebaseCloudServices.getInstance().getDatabase();

        mDbService.addChildListener(appInfoPath, mAppInfoListener);
        mDbService.addValueListener(pinPath, mPinListener);
    }

    @Override
    public void notify(Message message) {
        CloudMessage cloudMessage;
        switch (message.getTopic()) {
            case WRITE_TO_CLOUD:
                cloudMessage = (CloudMessage) message;
                mDbService.setValue(cloudMessage.getPath(), cloudMessage.getData(), new ResultListener<Void, DatabaseError>() {
                    @Override
                    public void onFailure(DatabaseError error) {
                        Log.d("Database access fail", error.name());
                    }
                });
                break;
            case DELETE_FROM_CLOUD:
                cloudMessage = (CloudMessage) message;
                mDbService.setValue(cloudMessage.getPath(), null, new ResultListener<Void, DatabaseError>() {
                    @Override
                    public void onFailure(DatabaseError error) {
                        Log.d("Database access fail", error.name());
                    }
                });
        }
    }

    @Override
    public void cleanUp() {
        mBus.unsubscribe(this, Topic.WRITE_TO_CLOUD);
        mBus.unsubscribe(this, Topic.DELETE_FROM_CLOUD);
        mDbService.removeValueListener(mPinListener);
        mDbService.removeChildListener(mAppInfoListener);
    }
}