package com.moqod.android.shaker.data;

import android.support.annotation.Nullable;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportsRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 16:41
 */

public class StubReportsRepository implements ReportsRepository {

    @Nullable
    @Override
    public ReportModel get(int id) {
        return TestReport.getExist();
    }

    @Nullable
    @Override
    public ReportModel put(ReportModel report) {
        return TestReport.getExist();
    }

    @Nullable
    @Override
    public ReportModel delete(int id) {
        return TestReport.getExist();
    }
}
