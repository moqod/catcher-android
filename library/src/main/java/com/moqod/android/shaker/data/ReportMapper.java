package com.moqod.android.shaker.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.moqod.android.shaker.data.db.ReportTable;
import com.moqod.android.shaker.domain.ReportModel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 14/08/2017
 * Time: 16:56
 */

public class ReportMapper {

    ContentValues map(ReportModel model) {
        ContentValues contentValues = new ContentValues(6);
        if (model.getId() > -1) {
            contentValues.put(ReportTable.COLUMN_ID, model.getId());
        }
        contentValues.put(ReportTable.COLUMN_DATE, model.getDate().getTime());
        contentValues.put(ReportTable.COLUMN_VERSION, model.getVersion());
        contentValues.put(ReportTable.COLUMN_COMMENT, model.getComment());
        contentValues.put(ReportTable.COLUMN_IMAGE, model.getImageUri());
        contentValues.put(ReportTable.COLUMN_LOGS, model.getLogsPath());
        return contentValues;
    }

    ReportModel reverseMap(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(ReportTable.COLUMN_ID));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(ReportTable.COLUMN_DATE));
        String version = cursor.getString(cursor.getColumnIndexOrThrow(ReportTable.COLUMN_VERSION));
        String comment = cursor.getString(cursor.getColumnIndexOrThrow(ReportTable.COLUMN_COMMENT));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(ReportTable.COLUMN_IMAGE));
        String logs = cursor.getString(cursor.getColumnIndexOrThrow(ReportTable.COLUMN_LOGS));
        return new ReportModel(id, new Date(date), version, comment, image, logs);
    }

}
