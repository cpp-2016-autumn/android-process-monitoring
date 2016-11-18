package com.appmon.client.initialization;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.appmon.client.initialization.login.LoginActivity;
import com.appmon.shared.DatabaseValueListener;
import com.appmon.shared.IDataSnapshot;
import com.appmon.shared.entities.DeviceInfo;
import com.appmon.shared.entities.PackageInfo;
import com.appmon.shared.firebase.FirebaseCloudServices;
import com.appmon.shared.IDatabaseService;
import com.appmon.shared.ResultListener;
import com.appmon.shared.DatabaseError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling service setup tasks.
 * Handles initialization and termination of blocking services.
 * Created by MikeSotnichek on 11/1/2016.
 */
public class SetupService extends IntentService {
    private static final String ACTION_INIT = "com.appmon.client.action.INIT";
    private static final String ACTION_TERMINATE = "com.appmon.client.action.TERMINATE";

    private static final String EXTRA_UID = "com.appmon.client.extra.UID";

    IDatabaseService mDatabaseService = FirebaseCloudServices.getInstance().getDatabase();

    public static void StartInit(Context context, String UserIDExtra) {
        Intent intent = new Intent(context, SetupService.class);
        intent.putExtra(EXTRA_UID, UserIDExtra);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    public static void StartTerm(Context context) {
        Intent intent = new Intent(context, SetupService.class);
        intent.setAction(ACTION_TERMINATE);
        context.startService(intent);
    }

    public SetupService() {
        super("SetupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                String uidParam = intent.getStringExtra(EXTRA_UID);
                handleInit(uidParam);
            } else if (ACTION_TERMINATE.equals(action)) {
                handleTerm();
            }
        }
    }

    /**
     * Handle initialization.
     */
    private void handleInit(String UserID) {
        //hide icon
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(this, LoginActivity.class);
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * Handle termination.
     */
    private void handleTerm() {
        //show icon
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, LoginActivity.class);
        p.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }
}
