package com.moqod.android.shaker.domain;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:29
 */

public class ReportModel {

    private int mId;
    private Date mDate;
    private String mVersion;
    private String mComment;
    private String mImageUri;
    private String mLogsPath;

    public ReportModel(int id, Date date, String version, String comment, String imageUri, String logsPath) {
        mId = id;
        mDate = date;
        mVersion = version;
        mComment = comment;
        mImageUri = imageUri;
        mLogsPath = logsPath;
    }

    public static ReportModel create(Date date, String version, String comment, String imageUri, String logsPath) {
        return new ReportModel(-1, date, version, comment, imageUri, logsPath);
    }

    public int getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public String getVersion() {
        return mVersion;
    }

    public String getComment() {
        return mComment;
    }

    public String getImageUri() {
        return mImageUri;
    }

    public String getLogsPath() {
        return mLogsPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportModel that = (ReportModel) o;

        if (mId != that.mId) return false;
        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        if (mVersion != null ? !mVersion.equals(that.mVersion) : that.mVersion != null) return false;
        if (mComment != null ? !mComment.equals(that.mComment) : that.mComment != null) return false;
        if (mImageUri != null ? !mImageUri.equals(that.mImageUri) : that.mImageUri != null) return false;
        return mLogsPath != null ? mLogsPath.equals(that.mLogsPath) : that.mLogsPath == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        result = 31 * result + (mVersion != null ? mVersion.hashCode() : 0);
        result = 31 * result + (mComment != null ? mComment.hashCode() : 0);
        result = 31 * result + (mImageUri != null ? mImageUri.hashCode() : 0);
        result = 31 * result + (mLogsPath != null ? mLogsPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mVersion='" + mVersion + '\'' +
                ", mComment='" + mComment + '\'' +
                ", mImageUri='" + mImageUri + '\'' +
                ", mLogsPath='" + mLogsPath + '\'' +
                '}';
    }
}
