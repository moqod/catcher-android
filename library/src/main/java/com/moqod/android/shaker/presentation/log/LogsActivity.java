package com.moqod.android.shaker.presentation.log;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.R;
import com.moqod.android.shaker.Schedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 22/08/2017
 * Time: 14:01
 */

public class LogsActivity extends AppCompatActivity {

    private static final String EXTRA_LOGS_PATH = "logs_path";

    public static Intent getIntent(Context context, String logsPath) {
        Intent intent = new Intent(context, LogsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_LOGS_PATH, logsPath);
        return intent;
    }

    private Disposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String logsPath = getIntent().getStringExtra(EXTRA_LOGS_PATH);

        final TextView logsText = findViewById(R.id.logs_text);
        logsText.setMovementMethod(ScrollingMovementMethod.getInstance());

        Schedulers schedulers = Injector.getInstance().getSchedulers();

        mDisposable = new LogsReader().readLogsFile(logsPath, 100)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe(new BiConsumer<String, Throwable>() {
                    @Override
                    public void accept(String s, Throwable throwable) throws Exception {
                        if (throwable == null) {
                            logsText.setText(s);
                        } else {
                            Toast.makeText(LogsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
