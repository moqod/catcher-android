package com.moqod.android.shaker.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 17/08/2017
 * Time: 11:52
 */

public class LogCatHelper {

    private Context mContext;

    public LogCatHelper(Context context) {
        mContext = context;
    }

    public File captureLogsToFile() throws IOException {
        File newLogFile = newLogFile();

        if (newLogFile != null) {
            String command = "logcat  -vtime -d -f" + newLogFile.toString();
            Runtime.getRuntime().exec(command);
            return newLogFile;
        }
        throw new IOException("can not create log file");
    }

    @Nullable
    private File newLogFile() {
        File logsPath = new File(mContext.getExternalCacheDir(), "logs");
        if (logsPath.exists() || logsPath.mkdirs()) {
            return new File(logsPath, System.currentTimeMillis() + ".txt");
        }

        return null;
    }

    public void deleteLogsFile(String logsPath) {
        File file = new File(logsPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
