package com.moqod.android.shaker.data.api;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 14:00
 */

public class ReportDto {

    private Date date;
    private String comment;
    private DeviceInfoDto deviceInfo;
    private int screenshot;
    private int logs;
    private String url;

    public ReportDto(Date date, String comment, DeviceInfoDto deviceInfo) {
        this.date = date;
        this.comment = comment;
        this.deviceInfo = deviceInfo;
    }

    public void setScreenshot(int screenshot) {
        this.screenshot = screenshot;
    }

    public void setLogs(int logs) {
        this.logs = logs;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public DeviceInfoDto getDeviceInfo() {
        return deviceInfo;
    }

    public String getUrl() {
        return url;
    }
}
