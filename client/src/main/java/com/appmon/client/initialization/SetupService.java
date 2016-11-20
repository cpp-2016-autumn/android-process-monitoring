package com.appmon.client.initialization;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;

import com.appmon.client.initialization.login.LoginActivity;
import com.appmon.client.subscribers.blocking.BlockingService;

/**
 * An {@link IntentService} for handling service setup tasks.
 * Handles initialization and termination of blocking services.
 */
public class SetupService extends IntentService {
    private static final String ACTION_INIT = "com.appmon.client.action.INIT";
    private static final String ACTION_TERMINATE = "com.appmon.client.action.TERMINATE";

    /**
     * Starts this service to handle initialization in the specified context.
     *
     * @param context A context to start this service in.
     */
    public static void StartInit(Context context) {
        Intent intent = new Intent(context, SetupService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    /**
     * Starts this service to handle termination in the specified context.
     *
     * @param context A context to start this service in.
     */
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
                handleInit();
            } else if (ACTION_TERMINATE.equals(action)) {
                handleTerm();
            }
        }
    }

    /**
     * Handles initialization.
     */
    private void handleInit() {
        //hide icon
        PackageManager pm = getPackageManager();
        ComponentName LoginComp = new ComponentName(this, LoginActivity.class);
        pm.setComponentEnabledSetting(LoginComp,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        ComponentName BlockComp = new ComponentName(this, BlockingService.class);
        pm.setComponentEnabledSetting(BlockComp,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * Handles termination.
     */
    private void handleTerm() {
        //show icon
        PackageManager pm = getPackageManager();
        ComponentName LoginComp = new ComponentName(this, LoginActivity.class);
        pm.setComponentEnabledSetting(LoginComp,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        ComponentName BlockComp = new ComponentName(this, BlockingService.class);
        pm.setComponentEnabledSetting(BlockComp,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
