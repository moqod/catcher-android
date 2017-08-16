package com.moqod.android.shaker.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.moqod.android.shaker.R;
import com.moqod.android.shaker.presentation.ReportActivity;

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
                PendingIntent.getActivity(mContext, 0, ReportActivity.getIntent(mContext, reportId), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent sendReportIntent =
                PendingIntent.getService(mContext, 1, SendReportService.getIntent(mContext, reportId), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent deleteReportIntent =
                PendingIntent.getService(mContext, 2, DeleteReportService.getIntent(mContext, reportId), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mContext, "default_channel")
                .setContentTitle(mContext.getString(R.string.NOTIFICATION_REPORT_CREATED))
                .setContentText(mContext.getString(R.string.NOTIFICATION_REPORT_INFO))
                .setContentIntent(openReportIntent)
                .setSmallIcon(R.drawable.ic_send)
                .setDeleteIntent(deleteReportIntent)
                .addAction(R.drawable.ic_send, mContext.getString(R.string.GENERAL_SEND), sendReportIntent)
                .addAction(R.drawable.ic_send, mContext.getString(R.string.GENERAL_DELETE), deleteReportIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(reportId, notification);
        } else {
            Log.d("NotificationHelper", "NotificationManager is null");
        }
    }
}
