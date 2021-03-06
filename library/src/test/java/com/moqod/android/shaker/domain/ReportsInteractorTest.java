package com.moqod.android.shaker.domain;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.data.StubReportsRepository;
import com.moqod.android.shaker.utils.ActivityInfoProvider;
import com.moqod.android.shaker.utils.LogCatHelper;
import com.moqod.android.shaker.utils.NotificationHelper;
import com.moqod.android.shaker.utils.ScreenShotHelper;
import com.moqod.android.shaker.utils.VersionProvider;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
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
    public static final String TEST_COMMENT = "test_comment";
    public static final String TEST_VERSION = "Test version";

    private ReportsInteractor mReportsInteractor;
    private NotificationHelper mMockNotificationHelper;
    private ScreenShotHelper mMockScreenShotHelper;
    private LogCatHelper mMockLogCatHelper;
    private ReportUploader mMockReportUploader;
    private ActivityInfoProvider<DeviceInfoModel> mMockDeviceInfoProvider;
    private VersionProvider mMockVersionProvider;

    @Before
    public void setUp() throws Exception {
        mMockNotificationHelper = createMockNotificationHelper();
        mMockScreenShotHelper = createMockScreenShotHelper();
        mMockLogCatHelper = createMockLogCatHelper();
        mMockReportUploader = createMockReportUploader();
        mMockDeviceInfoProvider = createMockDeviceInfoProvider();
        mMockVersionProvider = createMockVersionProvider();
        mReportsInteractor = new ReportsInteractor(createMockReportsRepository(), mMockNotificationHelper,
                mMockScreenShotHelper, mMockDeviceInfoProvider, mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);
    }

    @Test
    public void testCreateReport() throws Exception {
        Activity activity = new Activity();
        mReportsInteractor.createReport(activity);

        verify(mMockScreenShotHelper).takeScreenShot(activity);
        verify(mMockNotificationHelper).issueNotification(TestReport.UNKNOWN_ID);
        verify(mMockLogCatHelper).captureLogsToFile();
    }

    @Test
    public void testDeleteReport() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        ReportModel testReport = TestReport.getExist();
        when(reportsRepository.delete(testReport.getId())).thenReturn(testReport);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider(), mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        TestObserver<Void> testObserver = mReportsInteractor.deleteReport(testReport.getId()).test();
        testObserver.assertComplete();

        verify(reportsRepository).delete(testReport.getId());
        verify(mMockNotificationHelper).cancelNotification(testReport.getId());
        verify(mMockScreenShotHelper).deleteScreenShot(testReport.getImageUri());
        verify(mMockLogCatHelper).deleteLogsFile(testReport.getLogsPath());
    }

    @Test
    public void testDeleteReportThrowException() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        when(reportsRepository.delete(TestReport.UNKNOWN_ID)).thenThrow(new SQLiteException());

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider(), mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        TestObserver<Void> testObserver = mReportsInteractor.deleteReport(TestReport.UNKNOWN_ID).test();
        testObserver.assertError(SQLiteException.class);
    }

    @Test
    public void testGetReport() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        ReportModel testReport = TestReport.getExist();
        when(reportsRepository.get(TestReport.UNKNOWN_ID)).thenReturn(testReport);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider(), mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        TestObserver<ReportModel> test = mReportsInteractor.getReport(TestReport.UNKNOWN_ID).test();
        test.assertResult(testReport);
    }

    @Test
    public void testGetReportNotFound() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        when(reportsRepository.get(TestReport.UNKNOWN_ID)).thenReturn(null);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider(), mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        TestObserver<ReportModel> test = mReportsInteractor.getReport(TestReport.UNKNOWN_ID).test();
        test.assertError(ReportNotFoundException.class);
    }

    @Test
    public void testGetReportThrowException() throws Exception {
        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        Exception exception = new SQLiteException();
        when(reportsRepository.get(TestReport.UNKNOWN_ID)).thenThrow(exception);

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, createMockDeviceInfoProvider(), mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        TestObserver<ReportModel> test = mReportsInteractor.getReport(TestReport.UNKNOWN_ID).test();
        test.assertError(exception);
    }

    @Test
    public void testSendReport() throws Exception {
        ReportModel testReport = TestReport.getExist();
        DeviceInfoModel testDeviceInfo = TestReport.getTestDeviceInfo();

        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        when(reportsRepository.get(testReport.getId())).thenReturn(testReport);
        when(reportsRepository.delete(testReport.getId())).thenReturn(testReport);

        when(mMockDeviceInfoProvider.get()).thenReturn(testDeviceInfo);

        when(mMockReportUploader.uploadReport(any(ReportModel.class), any(DeviceInfoModel.class)))
                .thenReturn(Single.just(TestReport.getExist()));

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, mMockDeviceInfoProvider, mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        ReportModel updatedReport = new ReportModel(testReport.getId(), testReport.getDate(),
                testReport.getVersion(), TEST_COMMENT, testReport.getImageUri(), testReport.getLogsPath());

        TestObserver<Void> testObserver = mReportsInteractor.sendReport(testReport.getId(), TEST_COMMENT).test();
        testObserver.assertComplete();
        verify(mMockReportUploader).uploadReport(updatedReport, testDeviceInfo);
        verify(reportsRepository).delete(testReport.getId());
        verify(mMockNotificationHelper).cancelNotification(testReport.getId());
        verify(mMockScreenShotHelper).deleteScreenShot(testReport.getImageUri());
        verify(mMockLogCatHelper).deleteLogsFile(testReport.getLogsPath());
    }

    @Test
    public void testSendError() throws Exception {
        ReportModel testReport = TestReport.getExist();
        DeviceInfoModel testDeviceInfo = TestReport.getTestDeviceInfo();

        ReportsRepository reportsRepository = mock(ReportsRepository.class);
        when(reportsRepository.get(testReport.getId())).thenReturn(testReport);
        when(reportsRepository.delete(testReport.getId())).thenReturn(testReport);

        when(mMockDeviceInfoProvider.get()).thenReturn(testDeviceInfo);

        when(mMockReportUploader.uploadReport(testReport, testDeviceInfo))
                .thenReturn(Single.<ReportModel>error(new IOException()));

        ReportModel updatedReport = new ReportModel(testReport.getId(), testReport.getDate(),
                testReport.getVersion(), TEST_COMMENT, testReport.getImageUri(), testReport.getLogsPath());

        mReportsInteractor = new ReportsInteractor(reportsRepository, mMockNotificationHelper,
                mMockScreenShotHelper, mMockDeviceInfoProvider, mMockLogCatHelper, mMockReportUploader, mMockVersionProvider);

        TestObserver<Void> testObserver = mReportsInteractor.sendReport(testReport.getId(), "test_comment").test();
        testObserver.assertNotComplete();
        verify(mMockReportUploader).uploadReport(updatedReport, testDeviceInfo);
    }

    private ScreenShotHelper createMockScreenShotHelper() {
        ScreenShotHelper screenShotHelper = mock(ScreenShotHelper.class);
        when(screenShotHelper.takeScreenShot(any(Activity.class))).thenReturn(TEST_IMAGE_URI);
        return screenShotHelper;
    }

    private NotificationHelper createMockNotificationHelper() {
        return mock(NotificationHelper.class);
    }

    private ReportsRepository createMockReportsRepository() {
        return new StubReportsRepository();
    }

    private ActivityInfoProvider<DeviceInfoModel> createMockDeviceInfoProvider() {
        return mock(ActivityInfoProvider.class);
    }

    private ReportUploader createMockReportUploader() {
        return mock(ReportUploader.class);
    }

    private LogCatHelper createMockLogCatHelper() {
        LogCatHelper logCatHelper = mock(LogCatHelper.class);
        try {
            when(logCatHelper.captureLogsToFile()).thenReturn(new File("test_logs_file"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logCatHelper;
    }

    private VersionProvider createMockVersionProvider() {
        VersionProvider versionProvider = mock(VersionProvider.class);
        when(versionProvider.getVersion(any(Context.class))).thenReturn("test_version");
        return versionProvider;
    }
}