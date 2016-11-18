package com.appmon.client.subscribers;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
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
import com.appmon.shared.entities.DeviceInfo;
import com.appmon.shared.entities.PackageInfo;
import com.appmon.shared.firebase.FirebaseCloudServices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by Mike on 11/16/2016.
 */

public class SubscriberManager {
    private Context mContext;

    private CloudManager mCloudManager = null;
    private PackageUpdateManager mPackageSubscriber = null;
    private BlockerService mBlockerService = null;
    private Bus mBus = null;

    public SubscriberManager(Context context, BlockerService blocker) {
        mContext = context;
        mBlockerService = blocker;
        mBus = new Bus();

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String UserID = FirebaseCloudServices.getInstance().getAuth().getUser().getUserID();

        final String deviceInfoPath = UserID + "/devices/" + android_id;
        final String appsInfoPath = UserID + "/apps/" + android_id;
        final String infoString = Build.BRAND + " " + Build.MODEL;
        final String pinPath = UserID + "/pin";

        ResultListener<Void, DatabaseError> syncListener = new ResultListener<Void, DatabaseError>() {
            @Override
            public void onSuccess(Void value) {
                mPackageSubscriber = new PackageUpdateManager(mBus, appsInfoPath);
                IntentFilter filter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
                filter.addAction("android.intent.action.PACKAGE_REMOVED");
                filter.addDataScheme("package");
                mContext.registerReceiver(mPackageSubscriber, filter);

                mCloudManager = new CloudManager(mBus, appsInfoPath, pinPath);

                Log.d("MANAGER", "REGISTERED STUFF");
            }

            @Override
            public void onFailure(DatabaseError value) {

            }
        };

        syncInit(deviceInfoPath, appsInfoPath, infoString, syncListener);


    }

    private void syncInit(String deviceInfoPath, final String appInfoPath, String infoString, final ResultListener<Void, DatabaseError> syncListener) {
        final IDatabaseService cloudDb = FirebaseCloudServices.getInstance().getDatabase();

        //set device name
        cloudDb.setValue(deviceInfoPath, new DeviceInfo(infoString),
                new ResultListener<Void, DatabaseError>() {
                    @Override
                    public void onFailure(DatabaseError error) {
                        syncListener.onFailure(error);
                    }
                });

        cloudDb.addValueListener(appInfoPath, new DatabaseValueListener() {
            @Override
            public void onChanged(IDataSnapshot snapshot) {
                Set<PackageInfo> cloudPackages = new HashSet<>();
                if (snapshot.exists()) {
                    for (IDataSnapshot child : snapshot.getChildren()) {
                        cloudPackages.add(child.getValue(PackageInfo.class));
                    }
                }
                cloudDb.removeValueListener(this);

                PackageManager pm = mContext.getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                Set<PackageInfo> devicePackages = new HashSet<>();
                for (ApplicationInfo info : packages) {
                    devicePackages.add(new PackageInfo(
                            pm.getApplicationLabel(info).toString(),
                            info.packageName,
                            false));
                }

                //new device packages
                Set<PackageInfo> deviceOriginal = new HashSet<>();
                for (PackageInfo p : devicePackages)
                    deviceOriginal.add(
                            new PackageInfo(p.getName(), p.getPackageName(), p.isBlocked()));

                devicePackages.removeAll(cloudPackages);
                final Map<String, PackageInfo> dataToAdd = new HashMap<>();
                for (PackageInfo p : devicePackages) {
                    dataToAdd.put(Integer.toString(p.hashCode()), p);
                }

                //packages on cloud that no longer exist on device
                cloudPackages.removeAll(deviceOriginal);
                Map<String, PackageInfo> dataToDelete = new HashMap<>();
                for (PackageInfo p : cloudPackages) {
                    dataToDelete.put(Integer.toString(p.hashCode()), null);
                }

                if (!dataToAdd.isEmpty()) {
                    cloudDb.setValue(appInfoPath, dataToAdd, new ResultListener<Void, DatabaseError>() {
                        @Override
                        public void onFailure(DatabaseError error) {
                            syncListener.onFailure(error);
                        }
                    });
                }
                if (!dataToDelete.isEmpty()) {
                    cloudDb.setValue(appInfoPath, dataToDelete, new ResultListener<Void, DatabaseError>() {
                        @Override
                        public void onFailure(DatabaseError error) {
                            syncListener.onFailure(error);
                        }
                    });
                }
                syncListener.onSuccess(null);
            }
        });

        //get device application info


    }

    public Bus getBus() {
        return mBus;
    }

    public void cleanUp() {
        mCloudManager.cleanUp();
        mContext.unregisterReceiver(mPackageSubscriber);
        mBlockerService.cleanUp();
    }
}
