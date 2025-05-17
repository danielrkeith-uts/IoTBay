package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.ApplicationAccessLog;
import model.Enums.ApplicationAction;

public class ApplicationAccessLogDBManager {
    
    private static final String ADD_APPLICATION_ACCESS_LOG_STMT = 
        "INSERT INTO ApplicationAccessLog (UserId, ApplicationAction, DateTime) VALUES (?, ?, ?);";

    private static final String GET_APPLICATION_ACCESS_LOGS_STMT = 
        "SELECT * FROM ApplicationAccessLog WHERE UserId = ?;";

    private static final String ANONYMISE_APPLICATION_ACCESS_LOGS_STMT = 
        "UPDATE ApplicationAccessLog SET UserId = NULL WHERE UserId = ?;";

    private static final String DELETE_APPLICATION_ACCESS_LOG_STMT = 
        "DELETE FROM ApplicationAccessLog WHERE AccessLogId = ?;";

    private final Connection conn;

    private final PreparedStatement addApplicationAccessLogPs;
    private final PreparedStatement getApplicationAccessLogsPs;
    private final PreparedStatement anonymiseApplicationAccessLogsPs;
    private final PreparedStatement deleteApplicationAccessLogPs;

    public ApplicationAccessLogDBManager(Connection conn) throws SQLException {
        this.conn = conn;

        this.addApplicationAccessLogPs = conn.prepareStatement(ADD_APPLICATION_ACCESS_LOG_STMT);
        this.getApplicationAccessLogsPs = conn.prepareStatement(GET_APPLICATION_ACCESS_LOGS_STMT);
        this.anonymiseApplicationAccessLogsPs = conn.prepareStatement(ANONYMISE_APPLICATION_ACCESS_LOGS_STMT);
        this.deleteApplicationAccessLogPs = conn.prepareStatement(DELETE_APPLICATION_ACCESS_LOG_STMT);
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

        List<ApplicationAccessLog> applicationAccessLogs = new LinkedList<>();
        while (rs.next()) {
            applicationAccessLogs.add(new ApplicationAccessLog(
                rs.getInt("AccessLogId"),
                userId,
                rs.getTimestamp("DateTime"),
                ApplicationAction.fromInt(rs.getInt("ApplicationAction"))
            ));
        }

        return applicationAccessLogs;
    }

    public void anonymiseApplicationAccessLogs(int userId) throws SQLException {
        anonymiseApplicationAccessLogsPs.setInt(1, userId);
        anonymiseApplicationAccessLogsPs.executeUpdate();
    }

    public void deleteApplicationAccessLog(int accessLogId) throws SQLException {
        deleteApplicationAccessLogPs.setInt(1, accessLogId);
        deleteApplicationAccessLogPs.executeUpdate();
    }

    public ApplicationAccessLog getApplicationAccessLogById(int logId) throws SQLException {
        String sql = "SELECT * FROM ApplicationAccessLog WHERE AccessLogId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ApplicationAccessLog(
                        rs.getInt("AccessLogId"),
                        rs.getInt("UserId"),
                        rs.getTimestamp("DateTime"),
                        ApplicationAction.fromInt(rs.getInt("ApplicationAction"))
                    );
                }
            }
        }
        return null;
    }

    public void updateApplicationAccessLog(int logId, ApplicationAction newAction) throws SQLException {
        String sql = "UPDATE ApplicationAccessLog SET ApplicationAction = ? WHERE AccessLogId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newAction.toInt());
            ps.setInt(2, logId);
            ps.executeUpdate();
        }
    }
}
