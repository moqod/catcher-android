package com.moqod.android.shaker.data.api;

import com.moqod.android.shaker.domain.DeviceInfoModel;
import com.moqod.android.shaker.domain.ReportModel;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 17/08/2017
 * Time: 17:37
 */

public class ReportMapper {

    public ReportDto map(ReportModel model, DeviceInfoModel deviceInfoDto) {
        return new ReportDto(model.getDate(), model.getComment(), map(deviceInfoDto));
    }

    private DeviceInfoDto map(DeviceInfoModel model) {
        return new DeviceInfoDto(model.getName(), model.getOs(), model.getScreenSize(), model.getOrientation(),
                model.getAdditionalInfo());
    }

}
