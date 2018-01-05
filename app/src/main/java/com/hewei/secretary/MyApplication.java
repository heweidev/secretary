package com.hewei.secretary;

import android.app.Application;

/**
 * Created by fengyinpeng on 2018/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Network.getInstance().init();
    }
}
