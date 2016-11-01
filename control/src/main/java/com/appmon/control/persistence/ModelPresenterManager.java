package com.appmon.control.persistence;

import com.appmon.control.models.IUserModel;
import com.appmon.control.models.UserModel;
import com.appmon.control.presenters.ILoginPresenter;
import com.appmon.control.presenters.IWelcomePresenter;
import com.appmon.control.presenters.LoginPresenter;
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

    private ModelPresenterManager() {
        IUserModel mUserModel = new UserModel(/*FirebaseAppServer.getInstance()*/);
        mWelcomePresenter = new WelcomePresenter(mUserModel);
        mLoginPresenter = new LoginPresenter(mUserModel);
    }

    public IWelcomePresenter getWelcomePresenter() {
        return mWelcomePresenter;
    }

    public ILoginPresenter getLoginPresenter() {
        return mLoginPresenter;
    }

}
