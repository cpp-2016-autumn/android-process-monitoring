package com.appmon.client.subscribers.blocking;

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
import com.appmon.client.subscribers.ISubscriber;
import com.appmon.client.subscribers.SubscriberManager;
import com.appmon.shared.entities.PackageInfo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Listens for accessibility events, notifies if an app should be blocked.
 * It is an {@link ISubscriber}, subscribed to {@link Topic#APP_STATE_UPDATE} and {@link Topic#UNBLOCK_APP}.
 */
public class BlockingService extends AccessibilityService implements ISubscriber {

    /**
     * Other managers.
     */
    private SubscriberManager mSubscriberManager;
    /**
     * Information required to setup this service.
     */
    private AccessibilityServiceInfo mAccessibilityInfo;
    /**
     * Set of all packages synchronized with the cloud.
     */
    private Set<PackageInfo> mPackages;
    /**
     * Name of a package that was unlocked.
     * null if no package is unlocked at the moment.
     */
    private String mUnblockedPackageName = null;

    /**
     * A bus that manages communication between components
     */
    private static Bus mBus;

    public static Bus getBusInstance() {
        if (mBus == null)
            mBus = new Bus();
        return mBus;
    }


    public BlockingService() {
        //Ok
    }

    @Override
    public void onServiceConnected() {
        mPackages = new HashSet<>();

        mAccessibilityInfo = new AccessibilityServiceInfo();
        mAccessibilityInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        mAccessibilityInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
        mAccessibilityInfo.notificationTimeout = 50;
        setServiceInfo(mAccessibilityInfo);

        mSubscriberManager = new SubscriberManager(getBusInstance(), this.getApplicationContext(), this);

        mBus.subscribe(this, Topic.APP_STATE_UPDATE);
        mBus.subscribe(this, Topic.UNBLOCK_APP);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!event.getPackageName().equals(mUnblockedPackageName)) {
            mUnblockedPackageName = null;
        }
        if (mUnblockedPackageName == null) {
            for (PackageInfo p : mPackages) {
                if (p.getPackageName().equals(event.getPackageName())){
                    if (p.isBlocked()) {
                        mBus.publish(new Message<>(event.getPackageName(), Topic.BLOCK_APP));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        //Ok
    }

    @Override
    public void onDestroy() {
        mSubscriberManager.cleanUp();
        SetupService.StartTerm(this.getApplicationContext());
    }

    @Override
    public void notify(Message message) {
        switch (message.getTopic()) {
            case APP_STATE_UPDATE:
                PackageInfo info = (PackageInfo) message.getData();
                if (!mPackages.add(info)) {
                    mPackages.remove(info);
                    mPackages.add(info);
                }
                break;
            case UNBLOCK_APP:
                if (message.getData() != null) {
                    mUnblockedPackageName = message.getData().toString();
                } else {
                    mUnblockedPackageName = null;
                    if (android.os.Build.VERSION.SDK_INT < 16) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    } else {
                        performGlobalAction(GLOBAL_ACTION_HOME);
                    }
                }
                break;
        }
    }

    @Override
    public void cleanUp() {
        mBus.unsubscribe(this, Topic.APP_STATE_UPDATE);
        mBus.unsubscribe(this, Topic.UNBLOCK_APP);
    }
}
