package com.moqod.android.shaker.data;

import com.moqod.android.shaker.BuildConfig;
import com.moqod.android.shaker.Injector;
import com.moqod.android.shaker.TestReport;
import com.moqod.android.shaker.data.db.DbOpenHelper;
import com.moqod.android.shaker.domain.ReportModel;
import com.moqod.android.shaker.domain.ReportsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey Chuvashev
 * Date: 15/08/2017
 * Time: 15:02
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = "src/main/AndroidManifest.xml")
public class DbReportsRepositoryTest {

    private ReportsRepository mReportsRepository;
    private DbOpenHelper mDbOpenHelper;

    @Before
    public void setUp() throws Exception {
        Injector.init(RuntimeEnvironment.application);

        Injector injector = Injector.getInstance();
        mDbOpenHelper = injector.getDbOpenHelper();
        mReportsRepository = injector.getReportsRepository();
    }

    @After
    public void tearDown() throws Exception {
        mDbOpenHelper.close();
    }

    @Test
    public void testGetReturnNull() throws Exception {
        ReportModel model = mReportsRepository.get(TestReport.UNKNOWN_ID);
        assertNull(model);
    }

    @Test
    public void testPutNewReport() throws Exception {
        ReportModel newReport = TestReport.getNew();
        ReportModel insertedReport = mReportsRepository.put(newReport);

        checkReport(newReport, insertedReport, false);
    }

    @Test
    public void testUpdateReport() throws Exception {
        ReportModel insertedReport = mReportsRepository.put(TestReport.getNew());

        ReportModel updateReport = new ReportModel(insertedReport.getId(), new Date(), "new comment", "new image uri");
        ReportModel updatedReport = mReportsRepository.put(updateReport);

        checkReport(updateReport, updatedReport, true);
    }

    @Test
    public void testUpdateWithUnknownId() throws Exception {
        ReportModel updateReport = TestReport.getExist();
        ReportModel updatedReport = mReportsRepository.put(updateReport);
        checkReport(updateReport, updatedReport, true);
    }

    @Test
    public void testDelete() throws Exception {
        ReportModel insertedReport = mReportsRepository.put(TestReport.getNew());
        ReportModel deletedReport = mReportsRepository.delete(insertedReport.getId());

        checkReport(deletedReport, insertedReport, true);
    }

    @Test
    public void testDeleteUnknownId() throws Exception {
        ReportModel deletedReport = mReportsRepository.delete(TestReport.UNKNOWN_ID);
        assertNull(deletedReport);
    }

    private void checkReport(ReportModel firstReport, ReportModel secondReport, boolean checkId) {
        assertNotNull(firstReport);
        assertNotNull(secondReport);
        assertTrue(firstReport != secondReport); // should return immutable
        if (checkId) {
            assertEquals(firstReport.getId(), secondReport.getId());
        }
        assertEquals(firstReport.getDate(), secondReport.getDate());
        assertEquals(firstReport.getComment(), secondReport.getComment());
        assertEquals(firstReport.getImageUri(), secondReport.getImageUri());
    }

}