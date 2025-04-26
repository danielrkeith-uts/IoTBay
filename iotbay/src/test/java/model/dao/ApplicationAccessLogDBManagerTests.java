package model.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.ApplicationAccessLog;
import model.Enums.ApplicationAction;

public class ApplicationAccessLogDBManagerTests {
    private ApplicationAccessLogDBManager applicationAccessLogDBManager;

    public ApplicationAccessLogDBManagerTests() throws ClassNotFoundException, SQLException {
        this.applicationAccessLogDBManager = new ApplicationAccessLogDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetApplicationAccessLogs() {
        List<ApplicationAccessLog> applicationAccessLogs;
        try {
            applicationAccessLogs = applicationAccessLogDBManager.getApplicationAccessLogs(1);
        } catch (SQLException e) {
            Assert.fail();
            return;
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        ApplicationAccessLog log1 = applicationAccessLogs.get(0);
        ApplicationAccessLog log2 = applicationAccessLogs.get(1);

        Assert.assertEquals(ApplicationAction.LOGIN, log1.getApplicationAction());
        Assert.assertEquals("2025-04-26 12:00:00.000", dateFormatter.format(log1.getDateTime()));
        Assert.assertEquals(ApplicationAction.LOGOUT, log2.getApplicationAction());
        Assert.assertEquals("2025-04-26 12:05:00.000", dateFormatter.format(log2.getDateTime()));
    }
}
