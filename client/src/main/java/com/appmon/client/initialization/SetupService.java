package com.appmon.client.initialization;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.appmon.client.initialization.login.LoginActivity;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling service setup tasks.
 * Handles initialization and termination of blocking services.
 * Created by MikeSotnichek on 11/1/2016.
 */
public class SetupService extends IntentService {
    private static final String ACTION_INIT = "com.appmon.client.action.INIT";
    private static final String ACTION_TERMINATE = "com.appmon.client.action.TERMINATE";

    private static final String EXTRA_USERNAME = "com.appmon.client.extra.USERNAME";

    public static void StartInit(Context context, String usernameExtra) {
        Intent intent = new Intent(context, SetupService.class);
        intent.setAction(ACTION_INIT);
        intent.putExtra(EXTRA_USERNAME, usernameExtra);
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
                final String param1 = intent.getStringExtra(EXTRA_USERNAME);
                handleInit(param1);
            } else if (ACTION_TERMINATE.equals(action)) {
                handleTerm();
            }
        }
    }

    /**
     * Handle initialization.
     */
    private void handleInit(String userName) {
        //hide icon
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(this, LoginActivity.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        //get application packages
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo p : packages) {
            //TODO: send app info to Firebase using shared library
        }
    }

    /**
     * Handle termination.
     */
    private void handleTerm() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, LoginActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
