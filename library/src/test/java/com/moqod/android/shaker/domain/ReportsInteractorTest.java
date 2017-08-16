package com.moqod.android.shaker.domain;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.data.StubReportsRepository;
import com.moqod.android.shaker.utils.ActivityInfoProvider;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import io.reactivex.observers.TestObserver;
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
                mMockScreenShotHelper, createMockDeviceInfoProvider());
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
                mMockScreenShotHelper, createMockDeviceInfoProvider());

        TestObserver<Void> testObserver = mReportsInteractor.deleteReport(TestReport.UNKNOWN_ID).test();
        testObserver.assertComplete();

        verify(reportsRepository).delete(TestReport.UNKNOWN_ID);
        verify(mMockNotificationHelper).cancelNotification(TestReport.UNKNOWN_ID);
    }

    @Test
    public void testDeleteReportThrowException() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        when(reportsRepository.delete(TestReport.UNKNOWN_ID)).thenThrow(new SQLiteException());

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider());

        TestObserver<Void> testObserver = mReportsInteractor.deleteReport(TestReport.UNKNOWN_ID).test();
        testObserver.assertError(SQLiteException.class);
    }

    @Test
    public void testGetReport() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        ReportModel testReport = TestReport.getExist();
        when(reportsRepository.get(TestReport.UNKNOWN_ID)).thenReturn(testReport);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider());

        TestObserver<ReportModel> test = mReportsInteractor.getReport(TestReport.UNKNOWN_ID).test();
        test.assertResult(testReport);
    }

    @Test
    public void testGetReportNotFound() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        when(reportsRepository.get(TestReport.UNKNOWN_ID)).thenReturn(null);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider());

        TestObserver<ReportModel> test = mReportsInteractor.getReport(TestReport.UNKNOWN_ID).test();
        test.assertError(ReportNotFoundException.class);
    }

    @Test
    public void testGetReportThrowException() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        Exception exception = new SQLiteException();
        when(reportsRepository.get(TestReport.UNKNOWN_ID)).thenThrow(exception);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider());

        TestObserver<ReportModel> test = mReportsInteractor.getReport(TestReport.UNKNOWN_ID).test();
        test.assertError(exception);
    }

    @Test
    public void testSendReport() throws Exception {
        mReportsInteractor.sendReport(TestReport.UNKNOWN_ID).test()
                .assertError(RuntimeException.class);
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

    private ActivityInfoProvider<DeviceInfoModel> createMockDeviceInfoProvider() {
        return new ActivityInfoProvider<DeviceInfoModel>() {
            @Override
            public DeviceInfoModel get(Activity activity) {
                return TestReport.getTestDeviceInfo();
            }
        };
    }
}