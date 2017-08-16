package com.moqod.android.shaker.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.domain.ReportsInteractor;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 10:57
 */

public class DeleteReportService extends IntentService {

    private static final String EXTRA_REPORT_ID = "report_id";
    private static final String TAG = "SendReportService";

    public static Intent getIntent(Context context, int reportId) {
        Intent intent = new Intent(context, DeleteReportService.class);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        return intent;
    }

    private ReportsInteractor mReportsInteractor;

    public DeleteReportService() {
        super(DeleteReportService.class.getName());
        mReportsInteractor = Injector.getInstance().getReportsInteractor();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            Log.d(TAG, "intent is null");
            return;
        }

        int reportId = intent.getIntExtra(EXTRA_REPORT_ID, -1);

        if (reportId > -1) {
            mReportsInteractor.deleteReport(reportId);
        } else {
            Log.d(TAG, "invalid report id = " + reportId);
        }
    }
}