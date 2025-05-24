package model.dao;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.ApplicationAccessLog;
import model.Enums.ApplicationAction;

public class ApplicationAccessLogDBManager {
    private static final String ADD_SQL =
        "INSERT INTO ApplicationAccessLog (UserId, ApplicationAction, DateTime) VALUES (?, ?, ?);";
    private static final String GET_BY_USER_SQL =
        "SELECT AccessLogId, UserId, ApplicationAction, DateTime FROM ApplicationAccessLog WHERE UserId = ?;";
    private static final String GET_BY_ID_SQL =
        "SELECT AccessLogId, UserId, ApplicationAction, DateTime FROM ApplicationAccessLog WHERE AccessLogId = ?;";
    private static final String DELETE_SQL =
        "DELETE FROM ApplicationAccessLog WHERE AccessLogId = ?;";
    private static final String ANONYMIZE_SQL =
        "UPDATE ApplicationAccessLog SET UserId = NULL WHERE UserId = ?;";

    private final PreparedStatement addPs;
    private final PreparedStatement getByUserPs;
    private final PreparedStatement getByIdPs;
    private final PreparedStatement deletePs;
    private final PreparedStatement anonymizePs;

    public ApplicationAccessLogDBManager(Connection conn) throws SQLException {
        this.addPs         = conn.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS);
        this.getByUserPs   = conn.prepareStatement(GET_BY_USER_SQL);
        this.getByIdPs     = conn.prepareStatement(GET_BY_ID_SQL);
        this.deletePs      = conn.prepareStatement(DELETE_SQL);
        this.anonymizePs   = conn.prepareStatement(ANONYMIZE_SQL);
    }

    public void addApplicationAccessLog(int userId, ApplicationAccessLog log) throws SQLException {
        addPs.setInt(1, userId);
        addPs.setInt(2, log.getApplicationAction().toInt());
        addPs.setTimestamp(3, new Timestamp(log.getDateTime().getTime()));
        addPs.executeUpdate();

        try (ResultSet keys = addPs.getGeneratedKeys()) {
            if (keys.next()) {
                log.setAccessLogId(keys.getInt(1));   // now matches AccessLogId
                log.setUserId(userId);
            }
        }
    }

    public List<ApplicationAccessLog> getApplicationAccessLogs(int userId) throws SQLException {
        getByUserPs.setInt(1, userId);
        try (ResultSet rs = getByUserPs.executeQuery()) {
            List<ApplicationAccessLog> logs = new LinkedList<>();
            while (rs.next()) {
                logs.add(new ApplicationAccessLog(
                    rs.getInt("AccessLogId"),                // renamed
                    rs.getInt("UserId"),
                    new Date(rs.getTimestamp("DateTime").getTime()),
                    ApplicationAction.fromInt(rs.getInt("ApplicationAction"))
                ));
            }
            return logs;
        }
    }

    public ApplicationAccessLog getApplicationAccessLogById(int logId) throws SQLException {
        getByIdPs.setInt(1, logId);
        try (ResultSet rs = getByIdPs.executeQuery()) {
            if (rs.next()) {
                return new ApplicationAccessLog(
                    rs.getInt("AccessLogId"),                // renamed
                    rs.getInt("UserId"),
                    new Date(rs.getTimestamp("DateTime").getTime()),
                    ApplicationAction.fromInt(rs.getInt("ApplicationAction"))
                );
            }
            return null;
        }
    }

    public void deleteApplicationAccessLog(int logId) throws SQLException {
        deletePs.setInt(1, logId);
        deletePs.executeUpdate();
    }

    public void anonymiseApplicationAccessLogs(int userId) throws SQLException {
        anonymizePs.setInt(1, userId);
        anonymizePs.executeUpdate();
    }
}





