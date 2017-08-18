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
import io.reactivex.functions.BiFunction;
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
    public Single<ReportModel> uploadReport(final ReportModel report, final DeviceInfoModel deviceInfo) {
        return Single.zip(uploadScreenShot(report), uploadLog(report), mapReport(report, deviceInfo))
                .flatMap(createReport())
                .map(reverseMapReport(report));
    }

    private Function<ReportDto, SingleSource<? extends ReportDto>> createReport() {
        return new Function<ReportDto, SingleSource<? extends ReportDto>>() {
            @Override
            public SingleSource<? extends ReportDto> apply(ReportDto dto) throws Exception {
                return mRestApi.createReport(dto);
            }
        };
    }

    private BiFunction<FileDto, FileDto, ReportDto> mapReport(final ReportModel report, final DeviceInfoModel deviceInfo) {
        return new BiFunction<FileDto, FileDto, ReportDto>() {
            @Override
            public ReportDto apply(FileDto screenShotFile, FileDto logsFile) throws Exception {
                final ReportDto dto = mReportMapper.map(report, deviceInfo);
                dto.setScreenshot(screenShotFile.getId());
                dto.setLogs(logsFile.getId());

                return dto;
            }
        };
    }

    private Function<ReportDto, ReportModel> reverseMapReport(final ReportModel report) {
        return new Function<ReportDto, ReportModel>() {
            @Override
            public ReportModel apply(ReportDto reportDto) throws Exception {
                return report;
            }
        };
    }

    private Single<FileDto> uploadScreenShot(ReportModel report) {
        return mRestApi.uploadScreenShot(createPart(mContext, "image/jpg", "image.jpg",
                Uri.parse(report.getImageUri())));
    }

    private Single<FileDto> uploadLog(ReportModel report) {
        return mRestApi.uploadLog(createPart(mContext, "text/plain", "log.txt",
                Uri.fromFile(new File(report.getLogsPath()))));
    }

    private MultipartBody.Part createPart(Context context, String mimeType, String fileName, Uri uri) {
        UriRequestBody requestBody =
                new UriRequestBody(context, MediaType.parse(mimeType), uri);
        return MultipartBody.Part.createFormData("url", fileName, requestBody);
    }
}
