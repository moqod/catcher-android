package com.moqod.android.shaker;

import io.reactivex.Scheduler;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 15:34
 */

public class Schedulers {

    private Scheduler mIO;
    private Scheduler mMainThread;

    public Schedulers(Scheduler IO, Scheduler mainThread) {
        mIO = IO;
        mMainThread = mainThread;
    }

    public Scheduler io() {
        return mIO;
    }

    public Scheduler mainThread() {
        return mMainThread;
    }
}
