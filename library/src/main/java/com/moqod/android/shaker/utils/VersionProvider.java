package com.moqod.android.shaker.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.moqod.android.shaker.Logger;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 28/08/2017
 * Time: 14:28
 */

public class VersionProvider {

    public String getVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return String.format(Locale.ENGLISH, "%s (%d)", packageInfo.versionName, packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("can not take package info", e);
        }
        return "";
    }
}
