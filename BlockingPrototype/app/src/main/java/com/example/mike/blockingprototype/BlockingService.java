package com.example.mike.blockingprototype;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class BlockingService extends AccessibilityService {

    @Override
    public void onServiceConnected() {
        //load list of blocked apps
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.packageNames = new String[1];
//        info.packageNames[0] = "com.android.camera";
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
//        info.notificationTimeout = 100;
//        this.setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("eventPkgName", event.getPackageName().toString());
        Log.d("eventType", Integer.toString(event.getEventType()));
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            Intent intent = new Intent(this, UnlockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onInterrupt() {

    }

}
