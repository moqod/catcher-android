package com.moqod.android.shaker;

import android.content.Context;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moqod.android.shaker.data.DbReportsRepository;
import com.moqod.android.shaker.data.ReportMapper;
import com.moqod.android.shaker.data.RetrofitReportUploader;
import com.moqod.android.shaker.data.api.RestApi;
import com.moqod.android.shaker.data.db.DbOpenHelper;
import com.moqod.android.shaker.domain.ReportsInteractor;
import com.moqod.android.shaker.domain.ReportsRepository;
import com.moqod.android.shaker.presentation.ErrorMapper;
import com.moqod.android.shaker.utils.DeviceInfoProvider;
import com.moqod.android.shaker.utils.LogCatHelper;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:18
 */

public class Injector {

    private static Injector sINJECTOR;

    private Context mContext;
    private ReportsRepository mReportsRepository; // singleton
    private DbOpenHelper mDbOpenHelper; // singleton
    private NotificationHelper mNotificationHelper; // singleton
    private Schedulers mSchedulers; // singleton
    private RetrofitReportUploader mRetrofitReportUploader;

    private Injector(Context context) {
        mContext = context;
    }

    public static Injector getInstance() {
        return sINJECTOR;
    }

    public static void init(Context context) {
        sINJECTOR = new Injector(context);
    }

    public ReportsRepository getReportsRepository() {
        if (mReportsRepository == null) {
            mReportsRepository = new DbReportsRepository(getDbOpenHelper(), getReportMapper());
        }
        return mReportsRepository;
    }

    public ReportMapper getReportMapper() {
        return new ReportMapper();
    }

    public DbOpenHelper getDbOpenHelper() {
        if (mDbOpenHelper == null) {
            mDbOpenHelper = new DbOpenHelper(mContext);
        }
        return mDbOpenHelper;
    }

    public ReportsInteractor getReportsInteractor() {
        return new ReportsInteractor(getReportsRepository(), getNotificationHelper(), new ScreenShotHelper(mContext),
                getDeviceInfoProvider(), new LogCatHelper(mContext), getReportUploader());
    }

    public DeviceInfoProvider getDeviceInfoProvider() {
        return new DeviceInfoProvider(mContext);
    }

    private NotificationHelper getNotificationHelper() {
        if (mNotificationHelper == null) {
            mNotificationHelper = new NotificationHelper(mContext);
        }
        return mNotificationHelper;
    }

    public Schedulers getSchedulers() {
        if (mSchedulers == null) {
            mSchedulers = new Schedulers(io.reactivex.schedulers.Schedulers.io(), AndroidSchedulers.mainThread());
        }
        return mSchedulers;
    }

    private RetrofitReportUploader getReportUploader() {
        if (mRetrofitReportUploader == null) {
            mRetrofitReportUploader = new RetrofitReportUploader(mContext, getRestApi(), new com.moqod.android.shaker.data.api.ReportMapper());
        }
        return mRetrofitReportUploader;
    }

    private RestApi getRestApi() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(50, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(clientBuilder.build());

        return builder.build().create(RestApi.class);
    }

    public ErrorMapper getErrorMapper() {
        return new ErrorMapper(mContext);
    }
}
