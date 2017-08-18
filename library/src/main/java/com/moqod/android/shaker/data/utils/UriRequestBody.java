package com.moqod.android.shaker.data.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 30/03/16
 * Time: 17:11
 */
public class UriRequestBody extends RequestBody {

    private final Context mContext;
    private MediaType mMediaType;
    private final Uri mUri;

    public UriRequestBody(Context context, MediaType mediaType, Uri uri) {
        mContext = context;
        mMediaType = mediaType;
        mUri = uri;
    }

    @Override
    public long contentLength() throws IOException {
        mContext.grantUriPermission(mContext.getPackageName(), mUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        InputStream inputStream = mContext.getContentResolver().openInputStream(mUri);
        int len = 0;
        if (inputStream != null) {
            len = inputStream.available();
            inputStream.close();
        }

        return len;
    }

    @Override
    public MediaType contentType() {
        return mMediaType;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        Source source = null;
        try {
            mContext.grantUriPermission(mContext.getPackageName(), mUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream inputStream = mContext.getContentResolver().openInputStream(mUri);

            if (inputStream != null) {
                source = Okio.source(inputStream);
                sink.writeAll(source);
            }
        } finally {
            Util.closeQuietly(source);
        }
    }

}