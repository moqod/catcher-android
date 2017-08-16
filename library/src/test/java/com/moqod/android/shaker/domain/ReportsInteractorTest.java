package com.moqod.android.shaker.domain;

import android.app.Activity;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.data.StubReportsRepository;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 16:18
 */
public class ReportsInteractorTest {

    private static final String TEST_IMAGE_URI = "test_image_uri";

    private ReportsInteractor mReportsInteractor;
    private NotificationHelper mMockNotificationHelper;
    private ScreenShotHelper mMockScreenShotHelper;

    @Before
    public void setUp() throws Exception {
        mMockNotificationHelper = createMockNotificationHelper();
        mMockScreenShotHelper = createMockScreenShotHelper();
        mReportsInteractor = new ReportsInteractor(createMockReportsRepository(), mMockNotificationHelper,
                mMockScreenShotHelper);
    }

    @Test
    public void testNewReport() throws Exception {
        Activity activity = new Activity();
        mReportsInteractor.createReport(activity);

        verify(mMockScreenShotHelper).takeScreenShot(activity);
        verify(mMockNotificationHelper).issueNotification(TestReport.UNKNOWN_ID);
    }

    @Test
    public void testDeleteReport() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper);

        mReportsInteractor.deleteReport(TestReport.UNKNOWN_ID);

        verify(reportsRepository).delete(TestReport.UNKNOWN_ID);
    }

    private ScreenShotHelper createMockScreenShotHelper() {
        ScreenShotHelper screenShotHelper = mock(ScreenShotHelper.class);
        when(screenShotHelper.takeScreenShot(null)).thenReturn(TEST_IMAGE_URI);
        return screenShotHelper;
    }

    private NotificationHelper createMockNotificationHelper() {
        return mock(NotificationHelper.class);
    }

    private ReportsRepository createMockReportsRepository() {
        return new StubReportsRepository();
    }
}