package com.appmon.shared.firebase;

import com.appmon.shared.IAuthService;
import com.appmon.shared.ICloudServices;
import com.appmon.shared.IDatabaseService;

/**
 * {@link ICloudServices} implementation for Firebase backend.
 * Implemented as singleton class.
 */
public class FirebaseCloudServices implements ICloudServices {

    private static FirebaseCloudServices ourInstance = new FirebaseCloudServices();

    public static FirebaseCloudServices getInstance() {
        return ourInstance;
    }

    private IAuthService mAuthService;
    private IDatabaseService mDatabaseService;

    /**
     * Initializes subsystems
     */
    private FirebaseCloudServices() {
        mAuthService = new FirebaseAuthService();
        mDatabaseService = new FirebaseDatabaseService();
    }


    @Override
    public IAuthService getAuth() {
        return mAuthService;
    }

    @Override
    public IDatabaseService getDatabase() {
        return mDatabaseService;
    }
}
