package com.moqod.android.shaker.data.api;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 14:02
 */

public class DeviceInfoDto {

    private String name;
    private String os;
    private String screenSize;
    private int orientation; // 0 - portait, 1 - landscape
    private String additionalInfo;

    public DeviceInfoDto(String name, String os, String screenSize, int orientation, String additionalInfo) {
        this.name = name;
        this.os = os;
        this.screenSize = screenSize;
        this.orientation = orientation;
        this.additionalInfo = additionalInfo;
    }

    public String getName() {
        return name;
    }

    public String getOs() {
        return os;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public int getOrientation() {
        return orientation;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}
