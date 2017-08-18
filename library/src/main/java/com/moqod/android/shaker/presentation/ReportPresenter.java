package com.moqod.android.shaker.presentation;

import android.support.annotation.VisibleForTesting;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportsInteractor;
import com.moqod.android.shaker.utils.DeviceInfoProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 16/08/2017
 * Time: 15:10
 */

class ReportPresenter {

    private ReportsInteractor mReportsInteractor;
    private com.moqod.android.shaker.Schedulers mSchedulers;
    private int mReportId;
    private ErrorMapper mErrorMapper;
    private DeviceInfoProvider mDeviceInfoProvider;
    @VisibleForTesting
    ReportView mView;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    ReportPresenter(ReportsInteractor reportsInteractor, com.moqod.android.shaker.Schedulers schedulers,
                    int reportId, ErrorMapper errorMapper, DeviceInfoProvider deviceInfoProvider) {
        mReportsInteractor = reportsInteractor;
        mSchedulers = schedulers;
        mReportId = reportId;
        mErrorMapper = errorMapper;
        mDeviceInfoProvider = deviceInfoProvider;
    }

    void attachView(ReportView view) {
        mView = view;

        Disposable disposable = mReportsInteractor.getReport(mReportId)
                .map(new Function<ReportModel, ReportViewModel>() {
                    @Override
                    public ReportViewModel apply(ReportModel model) throws Exception {
                        return new ReportViewModel(model, mDeviceInfoProvider.get());
                    }
                })
                .subscribeOn(mSchedulers.io())
                .observeOn(mSchedulers.mainThread())
                .subscribe(new BiConsumer<ReportViewModel, Throwable>() {
                    @Override
                    public void accept(ReportViewModel reportViewModel, Throwable throwable) throws Exception {
                        if (throwable == null) {
                            mView.showReport(reportViewModel);
                        } else {
                            mView.showError(mErrorMapper.mapError(throwable));
                        }
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    void detachView() {
        mCompositeDisposable.clear();
        mView = null;
    }

    void sendReport(String comment) {
        Disposable disposable = mReportsInteractor.sendReport(mReportId, comment)
                .subscribeOn(mSchedulers.io())
                .observeOn(mSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.onReportSent();
                    }
                }, getErrorHandler());
        mCompositeDisposable.add(disposable);
    }

    void deleteReport() {
        Disposable disposable = mReportsInteractor.deleteReport(mReportId)
                .subscribeOn(mSchedulers.io())
                .observeOn(mSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.onReportDeleted();
                    }
                }, getErrorHandler());
        mCompositeDisposable.add(disposable);
    }

    private Consumer<Throwable> getErrorHandler() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showError(mErrorMapper.mapError(throwable));
            }
        };
    }
}
