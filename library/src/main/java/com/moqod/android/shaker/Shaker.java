package com.moqod.android.shaker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
import com.moqod.android.shaker.domain.ReportsInteractor;
import com.moqod.android.shaker.presentation.ReportActivity;
import com.squareup.seismic.ShakeDetector;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 13:54
 */

public class Shaker implements ShakeDetector.Listener, ActivityMonitor.OnAppForegroundListener {

    private static final String TAG = "Shaker";

    private final ShakeDetector mShakeDetector;
    private final ActivityMonitor mActivityMonitor;
    private final SensorManager mSensorManager;
    private final ReportsInteractor mReportsInteractor;

    public static Shaker init(Context context) {
        return new Shaker(context);
    }

    private Shaker(Context context) {
        Injector.init(context);

        mReportsInteractor = Injector.getInstance().getReportsInteractor();
        mActivityMonitor = new ActivityMonitor(this);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector(this);
        mShakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_LIGHT);

        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(mActivityMonitor);
    }

    @Override
    public void hearShake() {
        Activity currentActivity = mActivityMonitor.getCurrentActivity();
        Log.d(TAG, "hearShake, current activity is " + currentActivity);
        if (currentActivity != null && !(currentActivity instanceof ReportActivity)) {
            try {
                mReportsInteractor.createReport(currentActivity);
            } catch (IOException e) {
                Log.e(TAG, "can not create a report", e);
            }
        }
    }

    @Override
    public void onAppForegroundChanged(boolean foreground) {
        if (foreground) {
            mShakeDetector.start(mSensorManager);
        } else {
            mShakeDetector.stop();
        }
    }
}
