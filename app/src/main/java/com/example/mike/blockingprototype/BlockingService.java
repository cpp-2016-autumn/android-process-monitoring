package com.example.mike.blockingprototype;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.LinkedList;
import java.util.Stack;

public class BlockingService extends AccessibilityService {
    private Stack<String> unBlockedPackages = new Stack<>();

    @Override
    public void onServiceConnected() {
        //load list of blocked apps
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.packageNames = new String[4];
        info.packageNames[0] = "com.android.calculator2";
        info.packageNames[1] = "com.android.calendar";
        info.packageNames[2] = "com.android.gallery";
        info.packageNames[3] = "com.android.gallery3D";
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
        info.notificationTimeout = 50;
        this.setServiceInfo(info);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("eventPkgName", event.getPackageName().toString());
        Log.d("eventType", Integer.toString(event.getEventType()));
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            if (unBlockedPackages.contains(event.getPackageName())) {
                unBlockedPackages.remove(event.getPackageName());
                return;
            }
            Intent intent = new Intent(this, UnlockActivity.class);
            intent.putExtra("Unlock package", event.getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        int act = intent.getIntExtra("Unlock action", 0);
        switch (act){
            case 1:
                //unlockApp
                unBlockedPackages.push(intent.getStringExtra("Unlock package"));
                break;
            case 2:
                //app still locked
                performGlobalAction(GLOBAL_ACTION_HOME);
                break;
            default:
                break;
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onInterrupt() {

    }

}
