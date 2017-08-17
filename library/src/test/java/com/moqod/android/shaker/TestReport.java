package com.moqod.android.shaker;

import com.moqod.android.shaker.domain.DeviceInfoModel;
import com.moqod.android.shaker.domain.ReportModel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 15:47
 */

public class TestReport {

    public static final int UNKNOWN_ID = 100;

    public static ReportModel getNew() {
        return ReportModel.create(new Date(), "test comment", "test image uri", "logs path");
    }

    public static ReportModel getExist() {
        return new ReportModel(UNKNOWN_ID, new Date(), "new comment", "new image uri", "logs path");
    }

    public static DeviceInfoModel getTestDeviceInfo() {
        return new DeviceInfoModel("test name", "test os", "test screen size", 0, "test additional info");
    }

}
