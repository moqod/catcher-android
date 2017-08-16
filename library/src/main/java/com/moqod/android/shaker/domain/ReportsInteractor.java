package com.moqod.android.shaker.domain;

import android.app.Activity;
import android.util.Log;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;

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

    public ReportsInteractor(ReportsRepository reportsRepository, NotificationHelper notificationHelper,
                             ScreenShotHelper screenShotHelper) {
        mReportsRepository = reportsRepository;
        mNotificationHelper = notificationHelper;
        mScreenShotHelper = screenShotHelper;
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

    public void sendReport(int reportId) {

    }

    public void deleteReport(int reportId) {
        mReportsRepository.delete(reportId);
    }
}
