package com.moqod.android.shaker.presentation;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.moqod.android.shaker.presentation.report.ReportActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 16:17
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReportActivityTest {

    @Rule
    public ActivityTestRule<ReportActivity> activityRule = new ActivityTestRule<ReportActivity>(ReportActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            return ReportActivity.getIntent(null, 100);
        }
    };

    @Test
    public void testShowReport() throws Exception {

    }
}