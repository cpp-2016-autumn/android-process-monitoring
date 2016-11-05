package com.appmon.control;

import android.app.Application;
import android.content.Context;

public class ControlApp extends Application {
    static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    // get global app context
    public static Context getContext() {
        return ControlApp.sContext;
    }
}
