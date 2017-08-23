package com.moqod.android.shaker.presentation.report;

import com.moqod.android.shaker.domain.DeviceInfoModel;
import com.moqod.android.shaker.domain.ReportModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 11:22
 */

public class ReportViewModel {

    private ReportModel mModel;
    private DeviceInfoModel mDeviceInfoModel;
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);

    public ReportViewModel(ReportModel model, DeviceInfoModel deviceInfoModel) {
        mModel = model;
        mDeviceInfoModel = deviceInfoModel;
    }

    public String getDate() {
        return mDateFormat.format(mModel.getDate());
    }

    public String getDeviceInfo() {
        return String.format(Locale.ENGLISH, "%s; %s;\n%s;\n%s",
                mDeviceInfoModel.getName(),
                mDeviceInfoModel.getOs(),
                mDeviceInfoModel.getScreenSize(),
                mDeviceInfoModel.getAdditionalInfo()
        );
    }

    public String getComment() {
        return mModel.getComment();
    }

    public String getLogsPath() {
        return mModel.getLogsPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportViewModel that = (ReportViewModel) o;

        return mModel != null ? mModel.equals(that.mModel) : that.mModel == null;
    }

    @Override
    public int hashCode() {
        return mModel != null ? mModel.hashCode() : 0;
    }
}
