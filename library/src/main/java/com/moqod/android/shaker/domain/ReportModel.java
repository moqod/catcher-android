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
    private String mComment;
    private String mImageUri;

    public ReportModel(int id, Date date, String comment, String imageUri) {
        mId = id;
        mDate = date;
        mComment = comment;
        mImageUri = imageUri;
    }

    public static ReportModel create(Date date, String comment, String imageUri) {
        return new ReportModel(-1, date, comment, imageUri);
    }

    public int getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public String getComment() {
        return mComment;
    }

    public String getImageUri() {
        return mImageUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportModel that = (ReportModel) o;

        if (mId != that.mId) return false;
        if (mDate != null ? !mDate.equals(that.mDate) : that.mDate != null) return false;
        if (mComment != null ? !mComment.equals(that.mComment) : that.mComment != null) return false;
        return mImageUri != null ? mImageUri.equals(that.mImageUri) : that.mImageUri == null;
    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + (mDate != null ? mDate.hashCode() : 0);
        result = 31 * result + (mComment != null ? mComment.hashCode() : 0);
        result = 31 * result + (mImageUri != null ? mImageUri.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mComment='" + mComment + '\'' +
                ", mImageUri='" + mImageUri + '\'' +
                '}';
    }
}
