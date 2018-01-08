package com.hewei.secretary;

import android.util.Log;

/**
 * Created by fengyinpeng on 2018/1/8.
 */

public class Util {
    public static void logicError(String msg) {
        if (BuildConfig.DEBUG) {
            throw new IllegalStateException(msg);
        } else {
            Log.e("LogicError", msg);
        }
    }
}
