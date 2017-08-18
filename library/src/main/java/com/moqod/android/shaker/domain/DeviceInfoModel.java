package com.moqod.android.shaker.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 16:32
 */

public class DeviceInfoModel {

    private String mName;
    private String mOs;
    private String mScreenSize;
    private int mOrientation; // 0 - portait, 1 - landscape
    private String mAdditionalInfo;

    public DeviceInfoModel(String name, String os, String screenSize, int orientation, String additionalInfo) {
        mName = name;
        mOs = os;
        mScreenSize = screenSize;
        mOrientation = orientation;
        mAdditionalInfo = additionalInfo;
    }

    public String getName() {
        return mName;
    }

    public String getOs() {
        return mOs;
    }

    public String getScreenSize() {
        return mScreenSize;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public String getAdditionalInfo() {
        return mAdditionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceInfoModel that = (DeviceInfoModel) o;

        if (mOrientation != that.mOrientation) return false;
        if (mName != null ? !mName.equals(that.mName) : that.mName != null) return false;
        if (mOs != null ? !mOs.equals(that.mOs) : that.mOs != null) return false;
        if (mScreenSize != null ? !mScreenSize.equals(that.mScreenSize) : that.mScreenSize != null) return false;
        return mAdditionalInfo != null ? mAdditionalInfo.equals(that.mAdditionalInfo) : that.mAdditionalInfo == null;
    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + (mOs != null ? mOs.hashCode() : 0);
        result = 31 * result + (mScreenSize != null ? mScreenSize.hashCode() : 0);
        result = 31 * result + mOrientation;
        result = 31 * result + (mAdditionalInfo != null ? mAdditionalInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceInfoModel{" +
                "mName='" + mName + '\'' +
                ", mOs='" + mOs + '\'' +
                ", mScreenSize='" + mScreenSize + '\'' +
                ", mOrientation=" + mOrientation +
                ", mAdditionalInfo='" + mAdditionalInfo + '\'' +
                '}';
    }
}
