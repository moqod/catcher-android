package com.moqod.android.shaker.presentation.log;

import android.util.Log;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 22/08/2017
 * Time: 14:26
 */

public class LogsReader {

    private static final String TAG = "LogsReader";

    public Single<String> readLogsFile(final String path, final int lines) {
        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> singleEmitter) throws Exception {
                RandomAccessFile fileHandler = null;
                try {
                    fileHandler = new RandomAccessFile(path, "r");
                    long fileLength = fileHandler.length() - 1;
                    StringBuilder sb = new StringBuilder();
                    int line = 0;

                    for (long filePointer = fileLength; filePointer != -1; filePointer--) {
                        fileHandler.seek(filePointer);
                        int readByte = fileHandler.readByte();

                        if (readByte == 0xA) {
                            if (filePointer < fileLength) {
                                line = line + 1;
                            }
                        } else if (readByte == 0xD) {
                            if (filePointer < fileLength - 1) {
                                line = line + 1;
                            }
                        }
                        if (line >= lines) {
                            break;
                        }
                        sb.append((char) readByte);
                    }

                    singleEmitter.onSuccess(sb.reverse().toString());
                } catch (IOException e) {
                    singleEmitter.onError(e);
                } finally {
                    if (fileHandler != null)
                        try {
                            fileHandler.close();
                        } catch (IOException e) {
                            Log.e(TAG, "unable to read logs file", e);
                        }
                }
            }
        });
    }

}
