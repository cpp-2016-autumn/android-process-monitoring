package com.appmon.shared;

/**
 * Provides access to cloud services
 */
public interface ICloudServices {
    /**
     * @return {@link IAuthService} object
     */
    IAuthService getAuth();

    /**
     *
     * @return {@link IDatabaseService} service object
     */
    IDatabaseService getDatabase();
}
