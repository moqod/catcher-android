package com.moqod.android.shaker.presentation.report;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 15:11
 */

public interface ReportView {
    void showReport(ReportViewModel model);
    void showError(String text);
    void onReportDeleted();
    void onReportSent();
}
