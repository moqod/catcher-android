package com.moqod.android.shaker.data;

import android.content.ContentValues;
import com.moqod.android.shaker.BuildConfig;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.data.db.ReportTable;
import com.moqod.android.shaker.domain.ReportModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 15:45
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
public class ReportMapperTest {

    private ReportMapper mReportMapper;

    @Before
    public void setUp() throws Exception {
        Injector.init(RuntimeEnvironment.application);
        mReportMapper = Injector.getInstance().getReportMapper();
    }

    @Test
    public void testNewReport() throws Exception {
        ReportModel newReport = TestReport.getNew();
        ContentValues contentValues = mReportMapper.map(newReport);

        assertTrue(contentValues.size() == 3);
        assertEquals(newReport.getDate().getTime(), (long) contentValues.getAsLong(ReportTable.COLUMN_DATE));
        assertEquals(newReport.getComment(), contentValues.getAsString(ReportTable.COLUMN_COMMENT));
        assertEquals(newReport.getImageUri(), contentValues.getAsString(ReportTable.COLUMN_IMAGE));
    }

    @Test
    public void testExistReport() throws Exception {
        ReportModel newReport = TestReport.getExist();
        ContentValues contentValues = mReportMapper.map(newReport);

        assertTrue(contentValues.size() == 4);
        assertEquals(newReport.getId(), (int) contentValues.getAsInteger(ReportTable.COLUMN_ID));
        assertEquals(newReport.getDate().getTime(), (long) contentValues.getAsLong(ReportTable.COLUMN_DATE));
        assertEquals(newReport.getComment(), contentValues.getAsString(ReportTable.COLUMN_COMMENT));
        assertEquals(newReport.getImageUri(), contentValues.getAsString(ReportTable.COLUMN_IMAGE));
    }
}