package com.kobe.light;

import androidx.multidex.MultiDexApplication;

import com.kobe.lib_base.SpManager;


public class LightApplication extends MultiDexApplication {
    private static LightApplication mInstance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static String getToken() {
        return SpManager.getInstance(mInstance).get("token", "");
    }
}
