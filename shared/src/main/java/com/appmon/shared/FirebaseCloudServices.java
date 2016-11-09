package com.appmon.shared;

/**
 * Created by Mike on 11/10/2016.
 */
public class FirebaseCloudServices implements ICloudServices {
    private static FirebaseCloudServices ourInstance = new FirebaseCloudServices();

    public static FirebaseCloudServices getInstance() {
        return ourInstance;
    }

    private IAuthService mAuthService;
    private IDatabaseService mDatabaseService;

    private FirebaseCloudServices() {
        mAuthService = new FirebaseAuthService();
        //// TODO: 11/10/2016 implement IDatabaseService
    }


    @Override
    public IAuthService getAuth() {
        return mAuthService;
    }

    @Override
    public IDatabaseService getDatabase() {
        //// TODO: 11/10/2016 implement IDatabaseService
        throw new UnsupportedOperationException();
    }
}
