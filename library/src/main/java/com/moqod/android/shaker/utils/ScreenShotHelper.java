package com.moqod.android.shaker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 15:39
 */

public class ScreenShotHelper {

    private static final String TAG = "Shaker";

    private Context mContext;

    public ScreenShotHelper(Context context) {
        mContext = context;
    }

    public String takeScreenShot(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            View rootView = activity.getWindow().getDecorView().getRootView();
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);

            return MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", "");
        }
        Log.e(TAG, "permission WRITE_EXTERNAL_STORAGE is not granted");
        return null;
    }

    public void deleteScreenShot(String uri) {
        mContext.getContentResolver().delete(Uri.parse(uri), null, null);
    }

}
