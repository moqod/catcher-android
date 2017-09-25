package com.moqod.android.shaker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.SensorManager;
import com.moqod.android.shaker.domain.ReportsInteractor;
import com.moqod.android.shaker.presentation.report.ReportActivity;
import com.squareup.seismic.ShakeDetector;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 13:54
 */

public class Catcher implements ShakeDetector.Listener, ActivityMonitor.OnAppForegroundListener {

    private final ShakeDetector mShakeDetector;
    private final ActivityMonitor mActivityMonitor;
    private final SensorManager mSensorManager;
    private final ReportsInteractor mReportsInteractor;

    public static Catcher init(Context context, String apiToken) {
        return new Catcher(context, apiToken, 12);
    }

    public static Catcher init(Context context, String apiToken, int sensitivity) {
        return new Catcher(context, apiToken, sensitivity);
    }

    private Catcher(Context context, String token, int sensitivity) {
        Injector.init(context, token);

        mReportsInteractor = Injector.getInstance().getReportsInteractor();
        mActivityMonitor = new ActivityMonitor(this);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mShakeDetector = new ShakeDetector(this);
        mShakeDetector.setSensitivity(sensitivity);

        Application application = (Application) context.getApplicationContext();
        application.registerActivityLifecycleCallbacks(mActivityMonitor);
    }

    @Override
    public void hearShake() {
        Activity currentActivity = mActivityMonitor.getCurrentActivity();
        Logger.d("hearShake, current activity is " + currentActivity);
        if (currentActivity != null && !(currentActivity instanceof ReportActivity)) {
            try {
                mReportsInteractor.createReport(currentActivity);
            } catch (IOException e) {
                Logger.e("can not create a report", e);
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
