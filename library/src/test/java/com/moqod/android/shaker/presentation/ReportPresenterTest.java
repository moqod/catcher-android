package com.moqod.android.shaker.presentation;

import com.moqod.android.shaker.Schedulers;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportNotFoundException;
import com.moqod.android.shaker.domain.ReportsInteractor;
import io.reactivex.Completable;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    private ReportPresenter mReportPresenter;
    private ReportsInteractor mMockInteractor;

    @Before
    public void setUp() throws Exception {
        mMockInteractor = mock(ReportsInteractor.class);
        Schedulers schedulers = new Schedulers(io.reactivex.schedulers.Schedulers.trampoline(), io.reactivex.schedulers.Schedulers.trampoline());
        mReportPresenter = new ReportPresenter(mMockInteractor, schedulers, TestReport.UNKNOWN_ID);
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
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        ReportView reportView = mock(ReportView.class);

        mReportPresenter.attachView(reportView);

        assertNotNull(mReportPresenter.mView);
        verify(mMockInteractor).getReport(TestReport.UNKNOWN_ID);
        verify(reportView, only()).showReport(new ReportViewModel(testReport));
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
        verify(reportView, only()).showError(testException);
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
        verify(reportView).showError(testException);
    }

    // Send
    @Test
    public void testSendReport() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        when(mMockInteractor.sendReport(TestReport.UNKNOWN_ID))
                .thenReturn(Completable.complete());

        ReportView reportView = mock(ReportView.class);
        mReportPresenter.attachView(reportView);
        mReportPresenter.sendReport();

        verify(mMockInteractor).sendReport(TestReport.UNKNOWN_ID);
        verify(reportView).onReportSent();
    }

    @Test
    public void testSendReportShowError() throws Exception {
        ReportModel testReport = TestReport.getExist();
        when(mMockInteractor.getReport(TestReport.UNKNOWN_ID))
                .thenReturn(Single.just(testReport));
        ReportNotFoundException testException = new ReportNotFoundException();
        when(mMockInteractor.sendReport(TestReport.UNKNOWN_ID))
                .thenReturn(Completable.error(testException));

        ReportView reportView = mock(ReportView.class);
        mReportPresenter.attachView(reportView);
        mReportPresenter.sendReport();

        verify(mMockInteractor).sendReport(TestReport.UNKNOWN_ID);
        verify(reportView).showError(testException);
    }
}