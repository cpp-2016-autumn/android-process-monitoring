package com.appmon.control.models.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class AndroidSharedPreferences implements IPreferences {


    SharedPreferences mPreferences;

    public AndroidSharedPreferences(Context context) {
        mPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    public void remove(String key) {
        mPreferences.edit().remove(key).apply();
    }

    @Nullable
    @Override
    public String getString(String key) {
        return mPreferences.getString(key, null);
    }

    @Override
    public void setString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }
}
