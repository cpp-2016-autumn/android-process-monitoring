package com.appmon.control.models.user;

import android.support.annotation.Nullable;

public interface ISharedPreferences {
    void setString(String key, String value);
    @Nullable String getString(String key);
    void remove(String key);
}
