package com.moqod.android.shaker.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.moqod.android.shaker.BuildConfig;
import com.moqod.android.shaker.data.db.DbOpenHelper;
import com.moqod.android.shaker.data.db.ReportTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 13:35
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
public class DbOpenHelperTest {

    private DbOpenHelper mDbOpenHelper;
    private SQLiteDatabase mDb;

    @Before
    public void setUp() throws Exception {
        mDbOpenHelper = new DbOpenHelper(RuntimeEnvironment.application);
        mDb = mDbOpenHelper.getReadableDatabase();
    }

    @After
    public void tearDown() throws Exception {
        mDbOpenHelper.close();
    }

    @Test
    public void testDbOpen() throws Exception {
        SQLiteDatabase database = mDbOpenHelper.getWritableDatabase();
        assertTrue(database.isOpen());
    }

    @Test
    public void testColumns() throws Exception {
        Cursor cursor = mDb.query(ReportTable.NAME, null, null, null, null, null, null);
        assertNotNull(cursor);

        String[] columns = cursor.getColumnNames();

        assertEquals(columns.length, 4);

        assertThat(columns, hasItemInArray(ReportTable.COLUMN_ID));
        assertThat(columns, hasItemInArray(ReportTable.COLUMN_DATE));
        assertThat(columns, hasItemInArray(ReportTable.COLUMN_COMMENT));
        assertThat(columns, hasItemInArray(ReportTable.COLUMN_IMAGE));
    }
}