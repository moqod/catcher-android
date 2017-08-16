package com.moqod.android.shaker;

import android.content.Context;
import com.moqod.android.shaker.data.DbReportsRepository;
import com.moqod.android.shaker.data.ReportMapper;
import com.moqod.android.shaker.data.db.DbOpenHelper;
import com.moqod.android.shaker.domain.ReportsInteractor;
import com.moqod.android.shaker.domain.ReportsRepository;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:18
 */

public class Injector {

    private static Injector INJECTOR;

    private Context mContext;
    private ReportsRepository mReportsRepository; // singleton
    private DbOpenHelper mDbOpenHelper; // singleton
    private NotificationHelper mNotificationHelper; // singleton
    private Schedulers mSchedulers; // singleton

    private Injector(Context context) {
        mContext = context;
    }

    public static Injector getInstance() {
        return INJECTOR;
    }

    public static void init(Context context) {
        INJECTOR = new Injector(context);
    }

    public ReportsRepository getReportsRepository() {
        if (mReportsRepository == null) {
            mReportsRepository = new DbReportsRepository(getDbOpenHelper(), getReportMapper());
        }
        return mReportsRepository;
    }

    public ReportMapper getReportMapper() {
        return new ReportMapper();
    }

    public DbOpenHelper getDbOpenHelper() {
        if (mDbOpenHelper == null) {
            mDbOpenHelper = new DbOpenHelper(mContext);
        }
        return mDbOpenHelper;
    }

    public ReportsInteractor getReportsInteractor() {
        return new ReportsInteractor(getReportsRepository(), getNotificationHelper(), new ScreenShotHelper());
    }

    private NotificationHelper getNotificationHelper() {
        if (mNotificationHelper == null) {
            mNotificationHelper = new NotificationHelper(mContext);
        }
        return mNotificationHelper;
    }

    public Schedulers getSchedulers() {
        if (mSchedulers == null) {
            mSchedulers = new Schedulers(io.reactivex.schedulers.Schedulers.io(), AndroidSchedulers.mainThread());
        }
        return mSchedulers;
    }
}
