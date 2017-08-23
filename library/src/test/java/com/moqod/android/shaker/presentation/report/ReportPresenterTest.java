package com.moqod.android.shaker.presentation.report;

import com.moqod.android.shaker.Schedulers;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.domain.DeviceInfoModel;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportNotFoundException;
import com.moqod.android.shaker.domain.ReportsInteractor;
import com.moqod.android.shaker.presentation.ErrorMapper;
import com.moqod.android.shaker.presentation.report.ReportPresenter;
import com.moqod.android.shaker.presentation.report.ReportView;
import com.moqod.android.shaker.presentation.report.ReportViewModel;
import com.moqod.android.shaker.utils.DeviceInfoProvider;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 15:20
 */
public class ReportPresenterTest {

    public static final String TEST_COMMENT = "test_comment";
    public static final String TEST_ERROR_MESSAGE = "test_error_message";
    private ReportPresenter mReportPresenter;
    private ReportsInteractor mMockInteractor;
    private DeviceInfoProvider mMockDeviceInfoProvider;

    @Before
    public void setUp() throws Exception {
        mMockInteractor = mock(ReportsInteractor.class);
        mMockDeviceInfoProvider = mock(DeviceInfoProvider.class);
        Schedulers schedulers = new Schedulers(io.reactivex.schedulers.Schedulers.trampoline(), io.reactivex.schedulers.Schedulers.trampoline());
        mReportPresenter = new ReportPresenter(mMockInteractor, schedulers, TestReport.UNKNOWN_ID,
                createMockErrorMapper(), mMockDeviceInfoProvider);
    }

    private ErrorMapper createMockErrorMapper() {
        ErrorMapper errorMapper = mock(ErrorMapper.class);
        when(errorMapper.mapError(any(Throwable.class))).thenReturn(TEST_ERROR_MESSAGE);
        return errorMapper;
    }

    @Test
    public void testDetach() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        ReportView reportView = mock(ReportView.class);

        mReportPresenter.attachView(reportView);
        assertNotNull(mReportPresenter.mView);

        mReportPresenter.detachView();
        assertNull(mReportPresenter.mView);
    }

    @Test
    public void testAttachViewAndShowReport() throws Exception {
        ReportModel testReport = TestReport.getExist();
        DeviceInfoModel testDeviceInfo = TestReport.getTestDeviceInfo();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        when(mMockDeviceInfoProvider.get()).thenReturn(testDeviceInfo);

        ReportView reportView = mock(ReportView.class);

        mReportPresenter.attachView(reportView);

        assertNotNull(mReportPresenter.mView);
        verify(mMockInteractor).getReport(TestReport.UNKNOWN_ID);
        verify(reportView, only()).showReport(new ReportViewModel(testReport, testDeviceInfo));
        verifyNoMoreInteractions(reportView);
    }

    @Test
    public void testAttachViewAndShowError() throws Exception {
        ReportNotFoundException testException = new ReportNotFoundException();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.<ReportModel>error(testException));
        ReportView reportView = mock(ReportView.class);

        mReportPresenter.attachView(reportView);

        verify(mMockInteractor).getReport(TestReport.UNKNOWN_ID);
        verify(reportView, only()).showError(TEST_ERROR_MESSAGE);
        verifyNoMoreInteractions(reportView);
    }

    // delete
    @Test
    public void testDeleteReport() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        when(mMockInteractor.deleteReport(TestReport.UNKNOWN_ID))
                .thenReturn(Completable.complete());

        ReportView reportView = mock(ReportView.class);
        mReportPresenter.attachView(reportView);
        mReportPresenter.deleteReport();

        verify(mMockInteractor).deleteReport(TestReport.UNKNOWN_ID);
        verify(reportView).onReportDeleted();
    }

    @Test
    public void testDeleteReportShowError() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        ReportNotFoundException testException = new ReportNotFoundException();
        when(mMockInteractor.deleteReport(TestReport.UNKNOWN_ID))
                .thenReturn(Completable.error(testException));

        ReportView reportView = mock(ReportView.class);
        mReportPresenter.attachView(reportView);
        mReportPresenter.deleteReport();

        verify(mMockInteractor).deleteReport(TestReport.UNKNOWN_ID);
        verify(reportView).showError(TEST_ERROR_MESSAGE);
    }

    // Send
    @Test
    public void testSendReport() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        when(mMockInteractor.sendReport(TestReport.UNKNOWN_ID, TEST_COMMENT))
                .thenReturn(Completable.complete());

        ReportView reportView = mock(ReportView.class);
        mReportPresenter.attachView(reportView);
        mReportPresenter.sendReport(TEST_COMMENT);

        verify(mMockInteractor).sendReport(TestReport.UNKNOWN_ID, TEST_COMMENT);
        verify(reportView).onReportSent();
    }

    @Test
    public void testSendReportShowError() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        ReportNotFoundException testException = new ReportNotFoundException();
        when(mMockInteractor.sendReport(TestReport.UNKNOWN_ID, TEST_COMMENT))
                .thenReturn(Completable.error(testException));

        ReportView reportView = mock(ReportView.class);
        mReportPresenter.attachView(reportView);
        mReportPresenter.sendReport(TEST_COMMENT);

        verify(mMockInteractor).sendReport(TestReport.UNKNOWN_ID, TEST_COMMENT);
        verify(reportView).showError(TEST_ERROR_MESSAGE);
    }
}