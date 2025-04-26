package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.ApplicationAccessLog;
import model.Enums.ApplicationAction;

public class ApplicationAccessLogDBManager {
    private static final String GET_APPLICATION_ACCESS_LOGS_QUERY = "SELECT * FROM ApplicationAccessLog WHERE UserId = ?";

    private PreparedStatement getApplicationAccessLogsPs;

    public ApplicationAccessLogDBManager(Connection conn) throws SQLException {
        this.getApplicationAccessLogsPs = conn.prepareStatement(GET_APPLICATION_ACCESS_LOGS_QUERY);
    }

    public List<ApplicationAccessLog> getApplicationAccessLogs(int userId) throws SQLException {
        getApplicationAccessLogsPs.setInt(1, userId);

        ResultSet rs = getApplicationAccessLogsPs.executeQuery();

        List<ApplicationAccessLog> applicationAccessLogs = new LinkedList<ApplicationAccessLog>();
        while (rs.next()) {
            applicationAccessLogs.add(new ApplicationAccessLog(
                ApplicationAction.values()[rs.getInt("ApplicationAction")],
                new Date(rs.getTimestamp("DateTime").getTime())
            ));
        }

        return applicationAccessLogs;
    }
}
