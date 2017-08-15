package com.moqod.android.shaker.sample;

import android.app.Application;
import com.moqod.android.shaker.Shaker;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:14
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Shaker.init(this);
    }
}
