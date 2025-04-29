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
    private static final String ADD_APPLICATION_ACCESS_LOG_STMT = "INSERT INTO ApplicationAccessLog (UserId, ApplicationAction, DateTime) VALUES (?, ?, ?)";
    private static final String GET_APPLICATION_ACCESS_LOGS_STMT = "SELECT * FROM ApplicationAccessLog WHERE UserId = ?";

    private PreparedStatement addApplicationAccessLogPs;
    private PreparedStatement getApplicationAccessLogsPs;

    public ApplicationAccessLogDBManager(Connection conn) throws SQLException {
        this.addApplicationAccessLogPs = conn.prepareStatement(ADD_APPLICATION_ACCESS_LOG_STMT);
        this.getApplicationAccessLogsPs = conn.prepareStatement(GET_APPLICATION_ACCESS_LOGS_STMT);
    }

    public void addApplicationAccessLog(int userId, ApplicationAccessLog applicationAccessLog) throws SQLException {
        addApplicationAccessLogPs.setInt(1, userId);
        addApplicationAccessLogPs.setInt(2, applicationAccessLog.getApplicationAction().toInt());
        addApplicationAccessLogPs.setDate(3, new java.sql.Date(applicationAccessLog.getDateTime().getTime()));

        addApplicationAccessLogPs.executeUpdate();
    }

    public List<ApplicationAccessLog> getApplicationAccessLogs(int userId) throws SQLException {
        getApplicationAccessLogsPs.setInt(1, userId);

        ResultSet rs = getApplicationAccessLogsPs.executeQuery();

        List<ApplicationAccessLog> applicationAccessLogs = new LinkedList<ApplicationAccessLog>();
        while (rs.next()) {
            applicationAccessLogs.add(new ApplicationAccessLog(
                ApplicationAction.fromInt(rs.getInt("ApplicationAction")),
                new Date(rs.getTimestamp("DateTime").getTime())
            ));
        }

        return applicationAccessLogs;
    }
}
