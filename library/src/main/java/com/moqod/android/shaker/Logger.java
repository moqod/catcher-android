package com.moqod.android.shaker;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 28/08/2017
 * Time: 14:35
 */

public class Logger {

    public static final String TAG = "Shaker";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void e(String message) {
        Log.d(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.d(TAG, message, throwable);
    }

}
