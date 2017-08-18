package com.moqod.android.shaker.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.R;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:46
 */

public class ReportActivity extends AppCompatActivity implements ReportView {

    private static final String TAG = "ReportActivity";
    private static final String EXTRA_REPORT_ID = "report_id";

    public static Intent getIntent(Context context, int reportId) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_REPORT_ID, reportId);
        return intent;
    }

    private ReportPresenter mReportsPresenter;

    private TextView mDate;
    private TextView mDeviceInfo;
    private EditText mComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initViews();

        int reportId = getIntent().getIntExtra(EXTRA_REPORT_ID, -1);

        if (reportId > -1) {
            Injector injector = Injector.getInstance();
            mReportsPresenter = new ReportPresenter(injector.getReportsInteractor(), injector.getSchedulers(),
                    reportId, injector.getErrorMapper(), injector.getDeviceInfoProvider());
            mReportsPresenter.attachView(this);
        } else {
            Log.e(TAG, "report id is not defined");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.report_delete) {
            mReportsPresenter.deleteReport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReportsPresenter.detachView();
    }

    @Override
    public void showReport(ReportViewModel model) {
        mDate.setText(model.getDate());
        mDeviceInfo.setText(model.getDeviceInfo());
        mComment.setText(model.getComment());
    }

    @Override
    public void onReportDeleted() {
        finish();
    }

    @Override
    public void onReportSent() {
        finish();
    }

    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        mDate = findViewById(R.id.report_date);
        mDeviceInfo = findViewById(R.id.report_device_info);
        mComment = findViewById(R.id.report_comment);
        findViewById(R.id.report_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = mComment.getText().toString();
                mReportsPresenter.sendReport(comment);
            }
        });
    }
}
