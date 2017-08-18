package com.moqod.android.shaker.data;

import android.content.Context;
import android.net.Uri;
import com.moqod.android.shaker.data.api.FileDto;
import com.moqod.android.shaker.data.api.ReportDto;
import com.moqod.android.shaker.data.api.RestApi;
import com.moqod.android.shaker.data.utils.UriRequestBody;
import com.moqod.android.shaker.domain.DeviceInfoModel;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportUploader;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 17/08/2017
 * Time: 17:28
 */

public class RetrofitReportUploader implements ReportUploader {

    private Context mContext;
    private RestApi mRestApi;
    private com.moqod.android.shaker.data.api.ReportMapper mReportMapper;

    public RetrofitReportUploader(Context context, RestApi restApi,
                                  com.moqod.android.shaker.data.api.ReportMapper reportMapper) {
        mContext = context;
        mRestApi = restApi;
        mReportMapper = reportMapper;
    }

    @Override
    public Single<ReportModel> uploadReport(final ReportModel report, DeviceInfoModel deviceInfo) {
        final ReportDto dto = mReportMapper.map(report, deviceInfo);
        UriRequestBody screenShotRequestBody =
                new UriRequestBody(mContext, MediaType.parse("image/jpg"), Uri.parse(report.getImageUri()));
        return mRestApi.uploadScreenShot(MultipartBody.Part.createFormData("url", "image.jpg", screenShotRequestBody))
                .flatMap(new Function<FileDto, SingleSource<? extends ReportModel>>() {
                    @Override
                    public SingleSource<? extends ReportModel> apply(FileDto fileDto) throws Exception {
                        dto.setScreenshot(fileDto.getId());

                        Uri logsUri = Uri.fromFile(new File(report.getLogsPath()));
                        UriRequestBody logRequestBody =
                                new UriRequestBody(mContext, MediaType.parse("text/plain"), logsUri);
                        return mRestApi.uploadLog(MultipartBody.Part.createFormData("url", "log.txt", logRequestBody))
                                .flatMap(new Function<FileDto, SingleSource<? extends ReportModel>>() {
                                    @Override
                                    public SingleSource<? extends ReportModel> apply(FileDto fileDto) throws Exception {
                                        dto.setLogs(fileDto.getId());

                                        return mRestApi.createReport(dto)
                                                .map(new Function<Object, ReportModel>() {
                                                    @Override
                                                    public ReportModel apply(Object o) throws Exception {
                                                        return report;
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }
}
