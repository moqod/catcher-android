package com.moqod.android.shaker.utils;

import android.app.Activity;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 17:18
 */

public interface ActivityInfoProvider<T> {

    T get(Activity activity);

}
