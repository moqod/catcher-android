package com.moqod.android.shaker.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.moqod.android.shaker.Logger;
import com.moqod.android.shaker.R;
import com.moqod.android.shaker.presentation.report.ReportActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:36
 */

public class NotificationHelper {

    private Context mContext;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public void issueNotification(int reportId) {
        PendingIntent openReportIntent =
                PendingIntent.getActivity(mContext, reportId + 100, ReportActivity.getIntent(mContext, reportId), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent sendReportIntent =
                PendingIntent.getService(mContext, reportId + 101, SendReportService.getIntent(mContext, reportId), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent deleteReportIntent =
                PendingIntent.getService(mContext, reportId + 102, DeleteReportService.getIntent(mContext, reportId), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mContext, "default_channel")
                .setContentTitle(mContext.getString(R.string.NOTIFICATION_REPORT_CREATED))
                .setContentText(mContext.getString(R.string.NOTIFICATION_REPORT_INFO))
                .setContentIntent(openReportIntent)
                .setSmallIcon(R.drawable.ic_adb_white)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setDeleteIntent(deleteReportIntent)
                .addAction(R.drawable.ic_send, mContext.getString(R.string.GENERAL_SEND), sendReportIntent)
                .addAction(R.drawable.ic_clear, mContext.getString(R.string.GENERAL_DELETE), deleteReportIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(reportId, notification);
        } else {
            Logger.d("NotificationManager is null");
        }
    }

    public void cancelNotification(int reportId) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(reportId);
        } else {
            Logger.d("NotificationManager is null");
        }
    }
}
