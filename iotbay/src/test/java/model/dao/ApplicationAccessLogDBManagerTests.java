package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.ApplicationAccessLog;
import model.Enums.ApplicationAction;

public class ApplicationAccessLogDBManagerTests {
    // In database 
    private static final ApplicationAccessLog log1 = new ApplicationAccessLog(
        ApplicationAction.LOGIN,
        new GregorianCalendar(2025, 3, 26, 12, 0, 0).getTime()
    );
    private static final ApplicationAccessLog log2 = new ApplicationAccessLog(
        ApplicationAction.LOGOUT,
        new GregorianCalendar(2025, 3, 26, 12, 5, 0).getTime()
    );

    private static final ApplicationAccessLog log3 = new ApplicationAccessLog(
        ApplicationAction.ADD_TO_CART,
        new GregorianCalendar(2026, 1, 2, 3, 4, 5).getTime()
    );

    private final Connection conn;
    private final ApplicationAccessLogDBManager applicationAccessLogDBManager;

    public ApplicationAccessLogDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false); // To rollback changes made during test
        this.applicationAccessLogDBManager = new ApplicationAccessLogDBManager(conn);
    }

    @Test
    public void testAddApplicationAccessLog() {
        try {
            applicationAccessLogDBManager.addApplicationAccessLog(0, log3);

            List<ApplicationAccessLog> resultLogs = applicationAccessLogDBManager.getApplicationAccessLogs(0);

            Assert.assertTrue("Log should be added", resultLogs.contains(log3));
        } catch (SQLException e) {
            Assert.fail("Exception during add log test: " + e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testGetApplicationAccessLogs() {
        try {
            List<ApplicationAccessLog> expectedLogs = Arrays.asList(log1, log2);
            List<ApplicationAccessLog> resultLogs = applicationAccessLogDBManager.getApplicationAccessLogs(0);

            Assert.assertTrue("Logs should match expected", resultLogs.containsAll(expectedLogs));
        } catch (SQLException e) {
            Assert.fail("Exception during get logs test: " + e.getMessage());
        }
    }

    @Test
    public void testAnonymiseApplicationAccessLog() {
        try {
            applicationAccessLogDBManager.anonymiseApplicationAccessLogs(0);

            List<ApplicationAccessLog> resultLogs = applicationAccessLogDBManager.getApplicationAccessLogs(0);
            Assert.assertTrue("Logs should be anonymized (empty list)", resultLogs.isEmpty());
        } catch (SQLException e) {
            Assert.fail("Exception during anonymise log test: " + e.getMessage());
        } finally {
            try {
                conn.rollback(); 
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testDeleteApplicationAccessLog() {
        try {
            applicationAccessLogDBManager.deleteApplicationAccessLog(0);

            List<ApplicationAccessLog> expectedLogs = Arrays.asList(log2);
            List<ApplicationAccessLog> resultLogs = applicationAccessLogDBManager.getApplicationAccessLogs(0);

            Assert.assertTrue(resultLogs.containsAll(expectedLogs));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

}
