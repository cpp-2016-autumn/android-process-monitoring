package com.appmon.control.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.appmon.control.ControlApp;
import com.appmon.control.models.applistmodel.AppListModel;
import com.appmon.control.models.applistmodel.IAppListModel;
import com.appmon.control.models.devicelist.DeviceListModel;
import com.appmon.control.models.devicelist.IDeviceListModel;
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

    SharedPreferences mAndroidPref;

    private ModelManager() {
        if (ControlApp.getContext() != null) {
            mAndroidPref =
                    ControlApp.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        }
    }

    public synchronized IUserModel getUserModel() {
        if (mUserModel == null) {
            mUserModel = new UserModel(FirebaseCloudServices.getInstance(), mAndroidPref);
        }
        return mUserModel;
    }

    public synchronized IDeviceListModel getDeviceListModel() {
        if (mDeviceListModel == null) {
            mDeviceListModel = new DeviceListModel(FirebaseCloudServices.getInstance());
        }
        return mDeviceListModel;
    }

    public synchronized IAppListModel getAppListModel() {
        if (mAppListModel == null) {
            mAppListModel = new AppListModel(FirebaseCloudServices.getInstance());
        }
        return mAppListModel;
    }
}
