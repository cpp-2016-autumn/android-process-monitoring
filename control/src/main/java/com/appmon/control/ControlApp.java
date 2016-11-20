package com.appmon.control;

import android.app.Application;
import android.content.Context;

import com.appmon.shared.firebase.FirebaseCloudServices;

/**
 * Application class. Can be used for requesting
 * global objects.
 */
public class ControlApp extends Application {
    static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        // enable database caching globally
        FirebaseCloudServices.getInstance().getDatabase().setPersistence(true);
    }

    /**
     * Context getter
     * @return returns global app context
     */
    public static Context getContext() {
        return ControlApp.sContext;
    }
}
