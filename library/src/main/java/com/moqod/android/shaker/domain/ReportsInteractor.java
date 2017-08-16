package com.moqod.android.shaker.domain;

import android.app.Activity;
import android.util.Log;
import com.moqod.android.shaker.utils.ActivityInfoProvider;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:16
 */

public class ReportsInteractor {

    private ReportsRepository mReportsRepository;
    private NotificationHelper mNotificationHelper;
    private ScreenShotHelper mScreenShotHelper;
    private ActivityInfoProvider<DeviceInfoModel> mDeviceInfoProvider;

    public ReportsInteractor(ReportsRepository reportsRepository, NotificationHelper notificationHelper,
                             ScreenShotHelper screenShotHelper, ActivityInfoProvider<DeviceInfoModel> deviceInfoProvider) {
        mReportsRepository = reportsRepository;
        mNotificationHelper = notificationHelper;
        mScreenShotHelper = screenShotHelper;
        mDeviceInfoProvider = deviceInfoProvider;
    }

    public void createReport(Activity activity) {
        String imageUri = mScreenShotHelper.takeScreenShot(activity);

        ReportModel report = mReportsRepository.put(ReportModel.create(new Date(), "", imageUri));

        if (report != null) {
            mNotificationHelper.issueNotification(report.getId());
        } else {
            Log.d("ReportsInteractor", "cannot save report");
        }
    }

    public Completable sendReport(int reportId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                completableEmitter.onError(new RuntimeException("not implemented"));
            }
        });
    }

    public Completable deleteReport(final int reportId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                try {
                    mReportsRepository.delete(reportId);
                    mNotificationHelper.cancelNotification(reportId);
                    completableEmitter.onComplete();
                } catch (Exception e) {
                    completableEmitter.onError(e);
                }
            }
        });
    }

    public Single<ReportModel> getReport(final int id) {
        return Single.create(new SingleOnSubscribe<ReportModel>() {
            @Override
            public void subscribe(SingleEmitter<ReportModel> singleEmitter) throws Exception {
                try {
                    ReportModel reportModel = mReportsRepository.get(id);
                    if (reportModel != null) {
                        singleEmitter.onSuccess(reportModel);
                    } else {
                        singleEmitter.onError(new ReportNotFoundException());
                    }
                } catch (Exception e) {
                    singleEmitter.onError(e);
                }
            }
        });
    }
}
