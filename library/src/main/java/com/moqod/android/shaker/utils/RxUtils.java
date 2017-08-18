package com.moqod.android.shaker.utils;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 18/08/2017
 * Time: 16:12
 */

public class RxUtils {

    public static Action emtyAction() {
        return new Action() {
            @Override
            public void run() throws Exception {

            }
        };
    }

    public static <T> Consumer<T> emptyConsumer() {
        return new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {

            }
        };
    }

}
