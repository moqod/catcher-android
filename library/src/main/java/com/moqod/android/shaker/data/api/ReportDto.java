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
    private String version;
    private String comment;
    private DeviceInfoDto deviceInfo;
    private int screenshot;
    private int log;
    private String url;

    public ReportDto(Date date, String version, String comment, DeviceInfoDto deviceInfo) {
        this.date = date;
        this.version = version;
        this.comment = comment;
        this.deviceInfo = deviceInfo;
    }

    public Date getDate() {
        return date;
    }

    public String getVersion() {
        return version;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(int screenshot) {
        this.screenshot = screenshot;
    }

    public int getLogs() {
        return log;
    }

    public void setLogs(int logs) {
        this.log = logs;
    }
}
