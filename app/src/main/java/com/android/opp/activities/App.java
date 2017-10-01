package com.android.opp.activities;

import android.support.multidex.MultiDexApplication;

import com.jaychang.slm.SocialLoginManager;

/**
 * Created by camilo on 7/7/17.
 */

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SocialLoginManager.init(this);
    }
}

