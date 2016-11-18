package com.appmon.client.subscribers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Topic;
import com.appmon.shared.entities.PackageInfo;

public class PackageUpdateManager extends BroadcastReceiver {
    private static final String ACTION_ADDED = "android.intent.action.PACKAGE_ADDED";
    private static final String ACTION_REMOVED = "android.intent.action.PACKAGE_REMOVED";

    private static final String EXTRA_REPLACING = "EXTRA_REPLACING";


    private Bus mBus;
    private String mAppsInfoPath;

    public PackageUpdateManager(Bus bus, String appsInfoPath) {
        mBus = bus;
        mAppsInfoPath = appsInfoPath;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PACKAGE", "RECEIVED STUFF: " + intent.getAction());
        if(context == null) return;
        String action = intent.getAction();
        if (!intent.getBooleanExtra(EXTRA_REPLACING, false)) {
            if (action.equals(ACTION_ADDED)) {
                String packageName = intent.getData().toString().substring(8);
                PackageManager pm = context.getPackageManager();
                try {
                    ApplicationInfo info = pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA);
                    CloudMessage<PackageInfo> message = new CloudMessage<>(
                            new PackageInfo(
                                    pm.getApplicationLabel(info).toString(),
                                    info.packageName,
                                    false),
                            (mAppsInfoPath + "/" + Integer.toString(packageName.hashCode())),
                            Topic.WRITE_TO_CLOUD);
                    mBus.publish(message);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (action.equals(ACTION_REMOVED)) {
                String packageName = intent.getData().toString().substring(8);
                mBus.publish(new CloudMessage<>("",
                        mAppsInfoPath + "/" + Integer.toString(packageName.hashCode()),
                        Topic.DELETE_FROM_CLOUD));
            }
        }
    }

}
