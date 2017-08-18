package com.moqod.android.shaker.presentation;

import android.content.Context;
import com.moqod.android.shaker.R;
import com.moqod.android.shaker.domain.ReportNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 18/08/2017
 * Time: 16:18
 */

public class ErrorMapper {

    private Context mContext;

    public ErrorMapper(Context context) {
        mContext = context;
    }

    public String mapError(Throwable throwable) {
        if (throwable instanceof ReportNotFoundException) {
            return mContext.getString(R.string.ERROR_REPORT_NOT_FOUND);
        }
        return throwable.getMessage();
    }

}
