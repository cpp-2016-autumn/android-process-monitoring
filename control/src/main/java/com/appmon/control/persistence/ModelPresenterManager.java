package com.appmon.control.persistence;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.models.user.UserModel;
import com.appmon.control.presenters.ILoginPresenter;
import com.appmon.control.presenters.IRegisterPresenter;
import com.appmon.control.presenters.ISettingsPresenter;
import com.appmon.control.presenters.IWelcomePresenter;
import com.appmon.control.presenters.LoginPresenter;
import com.appmon.control.presenters.RegisterPresenter;
import com.appmon.control.presenters.SettingsPresenter;
import com.appmon.control.presenters.WelcomePresenter;


public class ModelPresenterManager {
    // Singleton
    private static ModelPresenterManager ourInstance = new ModelPresenterManager();
    public static ModelPresenterManager getInstance() {
        return ourInstance;
    }
    // === Persistent objects ===
    // Presenters
    private IWelcomePresenter mWelcomePresenter;
    private ILoginPresenter mLoginPresenter;
    private IRegisterPresenter mRegisterPresenter;
    private ISettingsPresenter mSettingsPresenter;

    private ModelPresenterManager() {
        IUserModel mUserModel = new UserModel(/*FirebaseAppServer.getInstance()*/);
        mWelcomePresenter = new WelcomePresenter(mUserModel);
        mLoginPresenter = new LoginPresenter(mUserModel);
        mRegisterPresenter = new RegisterPresenter(mUserModel);
        mSettingsPresenter = new SettingsPresenter(mUserModel);
    }

    public IWelcomePresenter getWelcomePresenter() {
        return mWelcomePresenter;
    }

    public ILoginPresenter getLoginPresenter() {
        return mLoginPresenter;
    }

    public IRegisterPresenter getRegisterPresenter() {
        return mRegisterPresenter;
    }

    public ISettingsPresenter getSettingsPresenter() {
        return  mSettingsPresenter;
    }
}
