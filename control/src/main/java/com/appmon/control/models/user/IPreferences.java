package com.appmon.control.models.user;

import android.support.annotation.Nullable;

/**
 * Interface, which represents some permanent
 * preferences storage
 */
public interface IPreferences {
    /**
     * Sets key to value
     * @param key key of settings pair
     * @param value value of settings pair
     */
    void setString(String key, String value);

    /**
     * Returns value of pair with key
     * @param key requested settings pair key
     * @return value of settings pair
     */
    @Nullable String getString(String key);

    /**
     * Removes settings pair with specified key
     * @param key settings pair key
     */
    void remove(String key);
}
