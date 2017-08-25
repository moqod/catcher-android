package com.moqod.android.shaker;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 13:54
 */

@SuppressWarnings("unused")
public class Shaker {

    public static Shaker init(Context context, String apiToken) {
        return new Shaker();
    }

    private Shaker() {
    }
}
