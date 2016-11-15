package com.appmon.control.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.appmon.control.ControlApp;
import com.appmon.control.models.devicelist.DeviceListModel;
import com.appmon.control.models.devicelist.IDeviceListModel;
import com.appmon.control.models.user.IUserModel;
import com.appmon.control.models.user.UserModel;
import com.appmon.control.presenters.DeviceListPresenter;
import com.appmon.control.presenters.IDeviceListPresenter;
import com.appmon.control.presenters.ILoginPresenter;
import com.appmon.control.presenters.IRegisterPresenter;
import com.appmon.control.presenters.ISettingsPresenter;
import com.appmon.control.presenters.IWelcomePresenter;
import com.appmon.control.presenters.LoginPresenter;
import com.appmon.control.presenters.RegisterPresenter;
import com.appmon.control.presenters.SettingsPresenter;
import com.appmon.control.presenters.WelcomePresenter;
import com.appmon.shared.firebase.FirebaseCloudServices;


/**
 * Singleton class, which creates presenters, objects and makes
 * linkage between them.
 *
 * All initializations are made lazy for avoiding big initial
 * application loading times.
 */
public class ModelPresenterManager {
    // Singleton
    private static ModelPresenterManager ourInstance = null;

    public static synchronized ModelPresenterManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new ModelPresenterManager();
        }
        return ourInstance;
    }
    // === Persistent objects ===
    // Models
    private IUserModel mUserModel = null;
    private IDeviceListModel mDeviceListModel = null;
    // Presenters
    private IWelcomePresenter mWelcomePresenter = null;
    private ILoginPresenter mLoginPresenter = null;
    private IRegisterPresenter mRegisterPresenter = null;
    private ISettingsPresenter mSettingsPresenter = null;
    private IDeviceListPresenter mDeviceListPresenter = null;

    private ModelPresenterManager() {
        SharedPreferences pref = null;
        if (ControlApp.getContext() != null) {
            pref = ControlApp.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        mUserModel = new UserModel(FirebaseCloudServices.getInstance(), pref);
    }

    private IUserModel getUserModel() {
        return mUserModel;
    }

    public synchronized IWelcomePresenter getWelcomePresenter() {
        if (mWelcomePresenter == null) {
            mWelcomePresenter = new WelcomePresenter(getUserModel());
        }
        return mWelcomePresenter;
    }

    public synchronized ILoginPresenter getLoginPresenter() {
        if (mLoginPresenter == null) {
            mLoginPresenter = new LoginPresenter(getUserModel());
        }
        return mLoginPresenter;
    }

    public synchronized IRegisterPresenter getRegisterPresenter() {
        if (mRegisterPresenter == null) {
            mRegisterPresenter = new RegisterPresenter(getUserModel());
        }
        return mRegisterPresenter;
    }

    public synchronized ISettingsPresenter getSettingsPresenter() {
        if (mSettingsPresenter == null) {
            mSettingsPresenter = new SettingsPresenter(getUserModel());
        }
        return  mSettingsPresenter;
    }

    public synchronized IDeviceListModel getDeviceListModel() {
        if (mDeviceListModel == null) {
            mDeviceListModel = new DeviceListModel(FirebaseCloudServices.getInstance());
        }
        return mDeviceListModel;
    }

    public synchronized IDeviceListPresenter getDeviceListPresenter() {
        if (mDeviceListPresenter == null) {
            mDeviceListPresenter = new DeviceListPresenter(getDeviceListModel());
        }
        return mDeviceListPresenter;
    }
}
