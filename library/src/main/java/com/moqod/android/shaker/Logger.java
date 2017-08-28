package com.moqod.android.shaker;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 28/08/2017
 * Time: 14:35
 */

public class Logger {

    private static final String TAG = "Shaker";

    private static boolean ENABLED = false;

    public static void enable() {
        ENABLED = true;
    }

    public static void disable() {
        ENABLED = false;
    }

    public static void d(String message) {
        if (ENABLED) {
            Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (ENABLED) {
            Log.d(TAG, message);
        }
    }

    public static void e(String message, Throwable throwable) {
        if (ENABLED) {
            Log.d(TAG, message, throwable);
        }
    }

}
