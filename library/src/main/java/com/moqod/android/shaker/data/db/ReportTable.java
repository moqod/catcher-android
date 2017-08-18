package com.moqod.android.shaker.data.db;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 12:02
 */

public final class ReportTable {

    public static final String NAME = "reports";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LOGS = "logs";

    public static final String[] COLUMNS = new String[] {
            COLUMN_ID, COLUMN_DATE, COLUMN_COMMENT, COLUMN_IMAGE, COLUMN_LOGS
    };

    public static String queryById(int id) {
        return ReportTable.COLUMN_ID + "=" + id;
    }

    private ReportTable() {
    }

    public static String getCreateStatement() {
        return "CREATE TABLE " + NAME + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " REAL, "
                + COLUMN_COMMENT + " TEXT, "
                + COLUMN_IMAGE + " TEXT, "
                + COLUMN_LOGS + " TEXT"
                + ");"
                ;
    }

}
