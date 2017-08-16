package com.moqod.android.shaker.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import com.moqod.android.shaker.domain.DeviceInfoModel;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 16:34
 */

public class DeviceInfoProvider implements ActivityInfoProvider<DeviceInfoModel> {

    @Override
    public DeviceInfoModel get(Activity activity) {
        int screenOrientation = activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? 0 : 1;
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        String displaySize = String.format(Locale.ENGLISH, "%dx%d", displayMetrics.widthPixels, displayMetrics.heightPixels);

        String additionalInfo =
                String.format(Locale.ENGLISH, "API Level: %d; Density: %s; Locale: %s;",
                        Build.VERSION.SDK_INT,
                        getDensityName(displayMetrics),
                        Locale.getDefault().getDisplayName());
        return new DeviceInfoModel(Build.MANUFACTURER + " " + android.os.Build.MODEL, String.format("Android %s", Build.VERSION.RELEASE),
                displaySize, screenOrientation, additionalInfo);
    }

    private String getDensityName(DisplayMetrics displayMetrics) {
        float density = displayMetrics.density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }
}
