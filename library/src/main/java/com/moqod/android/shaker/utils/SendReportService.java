package com.moqod.android.shaker.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.Logger;
import com.moqod.android.shaker.domain.ReportsInteractor;
import io.reactivex.functions.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 15:01
 */

public class SendReportService extends IntentService {

    private static final String EXTRA_REPORT_ID = "report_id";

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
            Logger.d("intent is null");
            return;
        }

        int reportId = intent.getIntExtra(EXTRA_REPORT_ID, -1);

        if (reportId > -1) {
            mReportsInteractor.sendReport(reportId, null).subscribe(RxUtils.emtyAction(), new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    // TODO: 18/08/2017 handle error
                    Logger.e("can not send the report", throwable);
                }
            });
        } else {
            Logger.d("invalid report id = " + reportId);
        }
    }
}
