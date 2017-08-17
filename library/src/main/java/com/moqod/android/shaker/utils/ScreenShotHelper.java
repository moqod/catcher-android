package com.moqod.android.shaker.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 15:39
 */

public class ScreenShotHelper {

    private Context mContext;

    public ScreenShotHelper(Context context) {
        mContext = context;
    }

    public String takeScreenShot(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        return MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "", "");
    }

    public void deleteScreenShot(String uri) {
        mContext.getContentResolver().delete(Uri.parse(uri), null, null);
    }

}
