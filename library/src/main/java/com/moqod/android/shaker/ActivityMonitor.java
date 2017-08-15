package com.moqod.android.shaker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 13:58
 */

class ActivityMonitor implements Application.ActivityLifecycleCallbacks {

    private OnAppForegroundListener mAppForegroundListener;

    @Nullable private Activity mActivity;
    private int mActiveActivityCount;

    ActivityMonitor(OnAppForegroundListener appForegroundListener) {
        mAppForegroundListener = appForegroundListener;
    }

    @Nullable
    public Activity getCurrentActivity() {
        return mActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mActivity = activity;
        mActiveActivityCount += 1;
        notifyAppForeground();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mActivity = null;
        mActiveActivityCount -= 1;
        notifyAppForeground();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    private void notifyAppForeground() {
        if (mActiveActivityCount <= 1) {
            mAppForegroundListener.onAppForegroundChanged(mActiveActivityCount == 1);
        }
    }

    interface OnAppForegroundListener {
        void onAppForegroundChanged(boolean foreground);
    }

}
