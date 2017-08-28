package com.moqod.android.shaker.utils.screenshot;

import android.view.View;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 28/08/2017
 * Time: 12:40
 */

class RootViewInfo {

    final View view;
    final WindowManager.LayoutParams layoutParams;
    final int top;
    final int left;

    RootViewInfo(View view, WindowManager.LayoutParams lp) {
        this.view = view;
        layoutParams = lp;
        int[] onScreenPosition = new int[2];
        view.getLocationOnScreen(onScreenPosition);
        left = onScreenPosition[0];
        top = onScreenPosition[1];
    }
}
