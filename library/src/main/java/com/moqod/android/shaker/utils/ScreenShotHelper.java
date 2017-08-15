package com.moqod.android.shaker.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 15:39
 */

public class ScreenShotHelper {

    public String takeScreenShot(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        return MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", "");
    }

}
