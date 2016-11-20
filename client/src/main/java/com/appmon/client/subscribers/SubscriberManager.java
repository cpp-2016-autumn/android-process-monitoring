package com.appmon.client.subscribers;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import com.appmon.client.bus.Bus;
import com.appmon.client.subscribers.blocking.BlockingController;
import com.appmon.client.subscribers.blocking.BlockingService;
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

/**
 * Handles initial setup of all other subscribers.
 * Synchronizes app data initially.
 */
public class SubscriberManager {
    private Context mContext;
    /**
     * List of applications blocked by default.
     */
    private static final String[] BLOCKED_BY_DEFAULT = {"com.android.settings", "com.android.packageinstaller"};

    private CloudManager mCloudManager = null;
    private PackageUpdateManager mPackageSubscriber = null;
    private BlockingService mBlockerService = null;
    private BlockingController mBlockingController = null;
    private Bus mBus = null;

    /**
     * Sets up all the components.
     * Except the {@link Bus} and the {@link BlockingService}, which are managed by the BlockingService itself.
     * @param bus A bus that was provided.
     * @param context This application's context.
     * @param blocker A blocking service that was provided.
     */
    public SubscriberManager(Bus bus, Context context, BlockingService blocker) {
        mContext = context;
        mBlockerService = blocker;
        mBus = bus;

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
                mBlockingController = new BlockingController(mBus, mContext);
            }
        };

        syncInit(deviceInfoPath, appsInfoPath, infoString, syncListener);

    }

    /**
     * Synchronizes application list on the device with the black list on the cloud.
     * @param deviceInfoPath Path to info about this device on the cloud.
     * @param appListPath Path application list on the cloud.
     * @param infoString Information about this device to be sent to the cloud.
     * @param syncListener A listener that will be notified when this task is finished.
     */
    private void syncInit(String deviceInfoPath, final String appListPath, String infoString, final ResultListener<Void, DatabaseError> syncListener) {
        final IDatabaseService cloudDb = FirebaseCloudServices.getInstance().getDatabase();

        //set device name
        cloudDb.setValue(deviceInfoPath, new DeviceInfo(infoString),
                new ResultListener<Void, DatabaseError>() {
                    @Override
                    public void onFailure(DatabaseError error) {
                        syncListener.onFailure(error);
                    }
                });

        cloudDb.addValueListener(appListPath, new DatabaseValueListener() {
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
                    boolean isBlockedByDefault = false;
                    for (String pn: BLOCKED_BY_DEFAULT) {
                        if (pn.equals(info.packageName)){
                            isBlockedByDefault = true;
                            break;
                        }
                    }
                    devicePackages.add(new PackageInfo(
                            pm.getApplicationLabel(info).toString(),
                            info.packageName,
                            isBlockedByDefault));
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
                    cloudDb.setValue(appListPath, dataToAdd, new ResultListener<Void, DatabaseError>() {
                        @Override
                        public void onFailure(DatabaseError error) {
                            syncListener.onFailure(error);
                        }
                    });
                }
                if (!dataToDelete.isEmpty()) {
                    cloudDb.setValue(appListPath, dataToDelete, new ResultListener<Void, DatabaseError>() {
                        @Override
                        public void onFailure(DatabaseError error) {
                            syncListener.onFailure(error);
                        }
                    });
                }
                syncListener.onSuccess(null);
            }
        });
    }

    /**
     * Cleans up subscribers.
     */
    public void cleanUp() {
        mCloudManager.cleanUp();
        mContext.unregisterReceiver(mPackageSubscriber);
        mBlockerService.cleanUp();
        mBlockingController.cleanUp();
    }
}
