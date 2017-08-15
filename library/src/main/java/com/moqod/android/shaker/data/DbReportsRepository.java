package com.moqod.android.shaker.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import com.moqod.android.shaker.data.db.DbOpenHelper;
import com.moqod.android.shaker.data.db.ReportTable;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportsRepository;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 14:21
 */

public class DbReportsRepository implements ReportsRepository {

    private final ReportMapper mReportMapper;
    private final SQLiteDatabase mDb;

    public DbReportsRepository(DbOpenHelper dbOpenHelper, ReportMapper reportMapper) {
        mReportMapper = reportMapper;
        mDb = dbOpenHelper.getWritableDatabase();
    }

    @Override
    @Nullable
    public ReportModel get(int id) {
        ReportModel result = null;
        Cursor cursor =
                mDb.query(ReportTable.NAME, ReportTable.COLUMNS, ReportTable.queryById(id), null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                result = mReportMapper.reverseMap(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    @Override
    @Nullable
    public ReportModel put(ReportModel report) {
        ContentValues contentValues = mReportMapper.map(report);
        int id = report.getId();
        if (id > -1) {
            Cursor cursor =
                    mDb.query(ReportTable.NAME, new String[]{ReportTable.COLUMN_ID}, ReportTable.queryById(id), null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                mDb.update(ReportTable.NAME, contentValues, ReportTable.queryById(id), null);
            } else {
                id = (int) mDb.insert(ReportTable.NAME, null, contentValues);
            }
        } else {
            id = (int) mDb.insert(ReportTable.NAME, null, contentValues);
        }
        return get(id);
    }

    @Override
    @Nullable
    public ReportModel delete(int id) {
        ReportModel reportModel = get(id);
        mDb.delete(ReportTable.NAME, ReportTable.queryById(id), null);
        return reportModel;
    }
}
