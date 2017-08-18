package com.moqod.android.shaker.data.api;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 17/08/2017
 * Time: 16:56
 */

public interface RestApi {

    @POST("report/") Single<ReportDto> createReport(@Body ReportDto reportDto);

    @POST("screenshot/")
    @Multipart
    Single<FileDto> uploadScreenShot(@Part MultipartBody.Part body);

    @POST("log/")
    @Multipart
    Single<FileDto> uploadLog(@Part MultipartBody.Part body);
}
