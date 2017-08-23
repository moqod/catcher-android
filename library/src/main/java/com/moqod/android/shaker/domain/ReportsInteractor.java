package com.moqod.android.shaker.domain;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import com.moqod.android.shaker.utils.ActivityInfoProvider;
import com.moqod.android.shaker.utils.LogCatHelper;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:16
 */

public class ReportsInteractor {

    private static final String TAG = "ReportsInteractor";

    private ReportsRepository mReportsRepository;
    private NotificationHelper mNotificationHelper;
    private ScreenShotHelper mScreenShotHelper;
    private ActivityInfoProvider<DeviceInfoModel> mDeviceInfoProvider;
    private LogCatHelper mLogCatHelper;
    private ReportUploader mReportUploader;

    public ReportsInteractor(ReportsRepository reportsRepository, NotificationHelper notificationHelper,
                             ScreenShotHelper screenShotHelper, ActivityInfoProvider<DeviceInfoModel> deviceInfoProvider,
                             LogCatHelper logCatHelper, ReportUploader reportUploader) {
        mReportsRepository = reportsRepository;
        mNotificationHelper = notificationHelper;
        mScreenShotHelper = screenShotHelper;
        mDeviceInfoProvider = deviceInfoProvider;
        mLogCatHelper = logCatHelper;
        mReportUploader = reportUploader;
    }

    public void createReport(Activity activity) throws IOException {
        String imageUri = mScreenShotHelper.takeScreenShot(activity);
        File logsFile = mLogCatHelper.captureLogsToFile();

        if (imageUri != null && logsFile != null) {
            ReportModel report = mReportsRepository.put(ReportModel.create(new Date(), "", imageUri, logsFile.toString()));

            if (report != null) {
                mNotificationHelper.issueNotification(report.getId());
                return;
            }
        }
        // TODO: 22/08/2017 issue error notification
        Log.d(TAG, "cannot save report");
    }

    public Completable sendReport(final int reportId, @Nullable final String comment) {
        return getReport(reportId)
                .flatMapCompletable(new Function<ReportModel, CompletableSource>() {
                    @Override
                    public CompletableSource apply(ReportModel model) throws Exception {
                        ReportModel updatedReport = model;
                        if (comment != null) {
                            updatedReport = new ReportModel(model.getId(), model.getDate(), comment, model.getImageUri(), model.getLogsPath());
                        }
                        return mReportUploader.uploadReport(updatedReport, mDeviceInfoProvider.get())
                                .flatMapCompletable(new Function<ReportModel, CompletableSource>() {
                                    @Override
                                    public CompletableSource apply(ReportModel model) throws Exception {
                                        return deleteReport(model.getId());
                                    }
                                });
                    }
                });
    }

    public Completable deleteReport(final int reportId) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                try {
                    ReportModel reportModel = mReportsRepository.delete(reportId);
                    if (reportModel != null) {
                        if (!mLogCatHelper.deleteLogsFile(reportModel.getLogsPath())) {
                            Log.e(TAG, "can not delete logs file for report " + reportId);
                        }
                        mScreenShotHelper.deleteScreenShot(reportModel.getImageUri());
                    }
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
