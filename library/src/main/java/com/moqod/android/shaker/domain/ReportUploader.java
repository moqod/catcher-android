package com.moqod.android.shaker.domain;

import io.reactivex.Single;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 17/08/2017
 * Time: 17:27
 */

public interface ReportUploader {

    Single<ReportModel> uploadReport(ReportModel report, DeviceInfoModel deviceInfo);

}
