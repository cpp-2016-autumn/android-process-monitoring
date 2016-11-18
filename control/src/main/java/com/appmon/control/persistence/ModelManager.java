package com.appmon.control.persistence;

import com.appmon.control.ControlApp;
import com.appmon.control.models.applist.AppListModel;
import com.appmon.control.models.applist.IAppListModel;
import com.appmon.control.models.devicelist.DeviceListModel;
import com.appmon.control.models.devicelist.IDeviceListModel;
import com.appmon.control.models.user.AndroidSharedPreferences;
import com.appmon.control.models.user.IPreferences;
import com.appmon.control.models.user.IUserModel;
import com.appmon.control.models.user.UserModel;
import com.appmon.shared.firebase.FirebaseCloudServices;


/**
 * Singleton class, which creates presenters, objects and makes
 * linkage between them.
 *
 * All initializations are made lazy for avoiding big initial
 * application loading times.
 */
public class ModelManager {
    // Singleton
    private static ModelManager ourInstance = null;

    public static synchronized ModelManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new ModelManager();
        }
        return ourInstance;
    }
    // === Persistent objects ===
    // Models
    private IUserModel mUserModel = null;
    private IDeviceListModel mDeviceListModel = null;
    private IAppListModel mAppListModel = null;

    IPreferences mAndroidPref;

    private ModelManager() {
        if (ControlApp.getContext() != null) {
            mAndroidPref = new AndroidSharedPreferences(ControlApp.getContext());
        }
    }

    /**
     * User model getter
     * @return returns user model instance (lazy init)
     */
    public synchronized IUserModel getUserModel() {
        if (mUserModel == null) {
            mUserModel = new UserModel(FirebaseCloudServices.getInstance(), mAndroidPref);
        }
        return mUserModel;
    }

    /**
     * Device list model getter
     * @return returns device list model instance (lazy init)
     */
    public synchronized IDeviceListModel getDeviceListModel() {
        if (mDeviceListModel == null) {
            mDeviceListModel = new DeviceListModel(FirebaseCloudServices.getInstance());
        }
        return mDeviceListModel;
    }

    /**
     * App list model getter
     * @return returns app list model instance (lazy init)
     */
    public synchronized IAppListModel getAppListModel() {
        if (mAppListModel == null) {
            mAppListModel = new AppListModel(FirebaseCloudServices.getInstance());
        }
        return mAppListModel;
    }
}
