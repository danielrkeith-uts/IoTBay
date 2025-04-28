package model.dao;

import java.sql.SQLException;
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

    private ApplicationAccessLogDBManager applicationAccessLogDBManager;

    public ApplicationAccessLogDBManagerTests() throws ClassNotFoundException, SQLException {
        this.applicationAccessLogDBManager = new ApplicationAccessLogDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetApplicationAccessLogs() {
        List<ApplicationAccessLog> applicationAccessLogs;
        try {
            applicationAccessLogs = applicationAccessLogDBManager.getApplicationAccessLogs(0);
        } catch (SQLException e) {
            Assert.fail();
            return;
        }

        ApplicationAccessLog log1Result = applicationAccessLogs.get(0);
        ApplicationAccessLog log2Result = applicationAccessLogs.get(1);

        Assert.assertEquals(log1, log1Result);
        Assert.assertEquals(log2, log2Result);
    }
}
