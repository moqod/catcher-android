package com.moqod.android.shaker.domain;

import android.support.annotation.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:17
 */

public interface ReportsRepository {

    @Nullable ReportModel get(int id);
    @Nullable ReportModel put(ReportModel report);
    @Nullable ReportModel delete(int id);
}
