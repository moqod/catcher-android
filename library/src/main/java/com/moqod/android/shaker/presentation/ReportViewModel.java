package com.moqod.android.shaker.presentation;

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
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);

    public ReportViewModel(ReportModel model) {
        mModel = model;
    }

    public String getDate() {
        return mDateFormat.format(mModel.getDate());
    }

    public String getDeviceInfo() {
        return "";
    }

    public String getComment() {
        return mModel.getComment();
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
