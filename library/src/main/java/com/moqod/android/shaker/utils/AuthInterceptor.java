package com.moqod.android.shaker.utils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 22/08/2017
 * Time: 13:44
 */

public class AuthInterceptor implements Interceptor {

    private String mToken;

    public AuthInterceptor(String token) {
        mToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Token " + mToken)
                .build();

        return chain.proceed(newRequest);

    }
}
