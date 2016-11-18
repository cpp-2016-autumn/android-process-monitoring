package com.appmon.client.subscribers;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.appmon.client.bus.Bus;
import com.appmon.client.bus.Message;
import com.appmon.client.bus.Topic;
import com.appmon.client.initialization.SetupService;
import com.appmon.client.initialization.login.LoginActivity;
import com.appmon.shared.entities.PackageInfo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BlockerService extends AccessibilityService implements ISubscriber {

    private SubscriberManager mSubscriberManager;
    private AccessibilityServiceInfo mAccessibilityInfo;
    private Set<PackageInfo> mPackages;
    private Bus mBus;

    public BlockerService() {

    }

    @Override
    public void onServiceConnected() {
        mPackages = new HashSet<>();

        mSubscriberManager = new SubscriberManager(this.getApplicationContext(), this);
        mBus = mSubscriberManager.getBus();

        mBus.subscribe(this, Topic.APP_STATE_UPDATE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mBus.publish(new Message<>(true, Topic.BLOCK_APP));
        Log.d("BLOCK", "Blocked app :" + event.getPackageName());
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        SetupService.StartTerm(this.getApplicationContext());
    }

    @Override
    public void notify(Message message) {
        PackageInfo info = (PackageInfo) message.getData();

        if(!mPackages.add(info)) {
            mPackages.remove(info);
            mPackages.add(info);
        }

        mAccessibilityInfo = new AccessibilityServiceInfo();
        mAccessibilityInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        mAccessibilityInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
        mAccessibilityInfo.notificationTimeout = 50;

        List<String> blockedPackages = new LinkedList<>();
        for (PackageInfo p: mPackages) {
            if (p.isBlocked()) blockedPackages.add(p.getPackageName());
        }
        mAccessibilityInfo.packageNames = null;
        mAccessibilityInfo.packageNames = blockedPackages.toArray(
                new String[blockedPackages.size()]);
        setServiceInfo(mAccessibilityInfo);
    }

    @Override
    public void cleanUp() {
        mBus.unsubscribe(this, Topic.APP_STATE_UPDATE);
    }
}
