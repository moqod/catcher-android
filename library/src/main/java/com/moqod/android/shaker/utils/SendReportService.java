package com.moqod.android.shaker.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.domain.ReportsInteractor;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 15:01
 */

public class SendReportService extends IntentService {

    private static final String EXTRA_REPORT_ID = "report_id";
    private static final String TAG = "SendReportService";

    public static Intent getIntent(Context context, int reportId) {
        Intent intent = new Intent(context, SendReportService.class);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        return intent;
    }

    private ReportsInteractor mReportsInteractor;

    public SendReportService() {
        super(SendReportService.class.getName());
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
            mReportsInteractor.sendReport(reportId).subscribe(new Action() {
                @Override
                public void run() throws Exception {

                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            });
        } else {
            Log.d(TAG, "invalid report id = " + reportId);
        }
    }
}
