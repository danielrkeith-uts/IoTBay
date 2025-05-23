package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import model.Staff;
import model.User;

/**
 * Data access manager for User, Customer, and Staff entities.
 * Assumes your Staff table now has columns:
 *   • StaffCardId   INTEGER
 *   • Admin         BOOLEAN
 *   • Position      TEXT
 */
public class UserDBManager {
    private final Connection conn;

    // — SQL statements —
    private static final String ADD_USER_SQL =
        "INSERT INTO User (FirstName, LastName, Email, Phone, Password) VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_CUSTOMER_SQL =
        "INSERT INTO Customer (UserId) VALUES (?);";
    private static final String ADD_STAFF_SQL =
        "INSERT INTO Staff (UserId, StaffCardId, Admin, Position) VALUES (?, ?, ?, ?);";

    private static final String USER_EXISTS_SQL =
        "SELECT 1 FROM User WHERE Email = ?;";

    private static final String GET_CUSTOMER_BY_CRED_SQL =
        "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, C.CartId " +
        "FROM User U JOIN Customer C ON U.UserId=C.UserId " +
        "WHERE U.Email=? AND U.Password=? LIMIT 1;";
    private static final String GET_CUSTOMER_BY_ID_SQL =
        "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, C.CartId " +
        "FROM User U JOIN Customer C ON U.UserId=C.UserId " +
        "WHERE U.UserId=? LIMIT 1;";

    private static final String GET_STAFF_BY_CRED_SQL =
        "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, " +
        "       S.StaffCardId, S.Admin, S.Position " +
        "FROM User U JOIN Staff S ON U.UserId=S.UserId " +
        "WHERE U.Email=? AND U.Password=? LIMIT 1;";
    private static final String GET_STAFF_BY_ID_SQL =
        "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, " +
        "       S.StaffCardId, S.Admin, S.Position " +
        "FROM User U JOIN Staff S ON U.UserId=S.UserId " +
        "WHERE U.UserId=? LIMIT 1;";

    private static final String SEARCH_STAFF_SQL =
        "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, " +
        "       S.StaffCardId, S.Admin, S.Position " +
        "FROM User U JOIN Staff S ON U.UserId=S.UserId " +
        "WHERE (U.FirstName || ' ' || U.LastName) LIKE ? " +
        "  AND (S.Position = ? OR ? = 'ALL');";

    private static final String GET_ALL_STAFF_SQL =
        "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, " +
        "       S.StaffCardId, S.Admin, S.Position " +
        "FROM User U JOIN Staff S ON U.UserId=S.UserId;";

    private static final String UPDATE_USER_SQL =
        "UPDATE User SET FirstName=?, LastName=?, Email=?, Phone=?, Password=? WHERE UserId=?;";
    private static final String UPDATE_STAFF_SQL =
        "UPDATE Staff SET StaffCardId=?, Admin=?, Position=? WHERE UserId=?;";

    private static final String DELETE_USER_SQL =
        "DELETE FROM User WHERE UserId=?;";

    // — PreparedStatements —
    private final PreparedStatement addUserPs;
    private final PreparedStatement addCustomerPs;
    private final PreparedStatement addStaffPs;
    private final PreparedStatement userExistsPs;
    private final PreparedStatement getCustomerByCredPs;
    private final PreparedStatement getCustomerByIdPs;
    private final PreparedStatement getStaffByCredPs;
    private final PreparedStatement getStaffByIdPs;
    private final PreparedStatement searchStaffPs;
    private final PreparedStatement getAllStaffPs;
    private final PreparedStatement updateUserPs;
    private final PreparedStatement updateStaffPs;
    private final PreparedStatement deleteUserPs;

    private final ApplicationAccessLogDBManager accessLogMgr;

    public UserDBManager(Connection conn) throws SQLException {
        this.conn                = conn;
        this.addUserPs           = conn.prepareStatement(ADD_USER_SQL, Statement.RETURN_GENERATED_KEYS);
        this.addCustomerPs       = conn.prepareStatement(ADD_CUSTOMER_SQL);
        this.addStaffPs          = conn.prepareStatement(ADD_STAFF_SQL);
        this.userExistsPs        = conn.prepareStatement(USER_EXISTS_SQL);
        this.getCustomerByCredPs = conn.prepareStatement(GET_CUSTOMER_BY_CRED_SQL);
        this.getCustomerByIdPs   = conn.prepareStatement(GET_CUSTOMER_BY_ID_SQL);
        this.getStaffByCredPs    = conn.prepareStatement(GET_STAFF_BY_CRED_SQL);
        this.getStaffByIdPs      = conn.prepareStatement(GET_STAFF_BY_ID_SQL);
        this.searchStaffPs       = conn.prepareStatement(SEARCH_STAFF_SQL);
        this.getAllStaffPs       = conn.prepareStatement(GET_ALL_STAFF_SQL);
        this.updateUserPs        = conn.prepareStatement(UPDATE_USER_SQL);
        this.updateStaffPs       = conn.prepareStatement(UPDATE_STAFF_SQL);
        this.deleteUserPs        = conn.prepareStatement(DELETE_USER_SQL);

        this.accessLogMgr        = new ApplicationAccessLogDBManager(conn);
    }

    // — Public API —

    /** Does an email already exist? */
    public boolean userExists(String email) throws SQLException {
        userExistsPs.setString(1, email);
        try (ResultSet rs = userExistsPs.executeQuery()) {
            return rs.next();
        }
    }

    /** Insert a new customer (auto-sets generated ID). */
    public void addCustomer(Customer c) throws SQLException {
        int uid = insertUser(c);
        addCustomerPs.setInt(1, uid);
        addCustomerPs.executeUpdate();
    }

    /** Insert a new staff (auto-sets ID, admin flag, position). */
    public void addStaff(Staff s) throws SQLException {
        int uid = insertUser(s);
        addStaffPs.setInt(1, uid);
        addStaffPs.setInt(2, s.getStaffCardId());
        addStaffPs.setBoolean(3, s.isAdmin());
        addStaffPs.setString(4, s.getPosition());
        addStaffPs.executeUpdate();
    }

    /** Fetch by credentials, preferring Customer then Staff. */
    public User getUser(String email, String password) throws SQLException {
        Customer c = getCustomerByCreds(email, password);
        return (c != null) ? c : getStaffByCreds(email, password);
    }

    /** Fetch by ID, preferring Customer then Staff. */
    public User getUser(int userId) throws SQLException {
        Customer c = getCustomerById(userId);
        return (c != null) ? c : getStaffById(userId);
    }

    /** Update a customer’s basic fields. */
    public void updateCustomer(Customer c) throws SQLException {
        updateUser(c);
    }

    /** Update staff’s fields + cardId/admin/position. */
    public void updateStaff(Staff s) throws SQLException {
        updateUser(s);
        updateStaffPs.setInt(1,    s.getStaffCardId());
        updateStaffPs.setBoolean(2, s.isAdmin());
        updateStaffPs.setString(3,  s.getPosition());
        updateStaffPs.setInt(4,    s.getUserId());
        updateStaffPs.executeUpdate();
    }

    /** Fully delete a user (annonymise their logs first). */
    public void deleteUser(int userId) throws SQLException {
        accessLogMgr.anonymiseApplicationAccessLogs(userId);
        deleteUserPs.setInt(1, userId);
        deleteUserPs.executeUpdate();
    }

    /** Soft-deactivate a staff so they can’t log in. */
    public void deactivateStaff(int userId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
             "UPDATE User SET Deactivated=1 WHERE UserId=?;")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /** Reverse soft-delete on a staff. */
    public void reactivateStaff(int userId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
             "UPDATE User SET Deactivated=0 WHERE UserId=?;")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /** Return every staff row. */
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> list = new ArrayList<>();
        try (ResultSet rs = getAllStaffPs.executeQuery()) {
            while (rs.next()) {
                list.add(fromStaffRow(rs));
            }
        }
        return list;
    }

    /**
     * Search by full name (LIKE %q%) and position (exact or “ALL” for no filter).
     */
    public List<Staff> searchStaff(String q, String position) throws SQLException {
        String namePattern = (q == null || q.trim().isEmpty())
                             ? "%" : "%" + q.trim() + "%";
        String posFilter   = (position == null || position.trim().isEmpty())
                             ? "ALL" : position.trim();

        searchStaffPs.setString(1, namePattern);
        searchStaffPs.setString(2, posFilter);
        searchStaffPs.setString(3, posFilter);

        List<Staff> list = new ArrayList<>();
        try (ResultSet rs = searchStaffPs.executeQuery()) {
            while (rs.next()) {
                list.add(fromStaffRow(rs));
            }
        }
        return list;
    }

    // — Private helpers —

    /** Inserts into User table, returns generated key (and sets u.userId). */
    private int insertUser(User u) throws SQLException {
        addUserPs.setString(1, u.getFirstName());
        addUserPs.setString(2, u.getLastName());
        addUserPs.setString(3, u.getEmail());
        addUserPs.setString(4, u.getPhone());
        addUserPs.setString(5, u.getPassword());
        addUserPs.executeUpdate();

        try (ResultSet rs = addUserPs.getGeneratedKeys()) {
            if (!rs.next()) throw new SQLException("Inserting User failed, no key");
            int id = rs.getInt(1);
            u.setUserId(id);
            return id;
        }
    }

    private Customer getCustomerByCreds(String email, String pw) throws SQLException {
        getCustomerByCredPs.setString(1, email);
        getCustomerByCredPs.setString(2, pw);
        try (ResultSet rs = getCustomerByCredPs.executeQuery()) {
            if (!rs.next()) return null;
            Customer c = new Customer(
                rs.getInt("UserId"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("Password")
            );
            c.setDeactivated(rs.getBoolean("Deactivated"));
            return c;
        }
    }

    private Customer getCustomerById(int uid) throws SQLException {
        getCustomerByIdPs.setInt(1, uid);
        try (ResultSet rs = getCustomerByIdPs.executeQuery()) {
            if (!rs.next()) return null;
            Customer c = new Customer(
                rs.getInt("UserId"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("Password")
            );
            c.setDeactivated(rs.getBoolean("Deactivated"));
            return c;
        }
    }

    private Staff getStaffByCreds(String email, String pw) throws SQLException {
        getStaffByCredPs.setString(1, email);
        getStaffByCredPs.setString(2, pw);
        try (ResultSet rs = getStaffByCredPs.executeQuery()) {
            if (!rs.next()) return null;
            return fromStaffRow(rs);
        }
    }

    private Staff getStaffById(int uid) throws SQLException {
        getStaffByIdPs.setInt(1, uid);
        try (ResultSet rs = getStaffByIdPs.executeQuery()) {
            if (!rs.next()) return null;
            return fromStaffRow(rs);
        }
    }

    private Staff fromStaffRow(ResultSet rs) throws SQLException {
        Staff s = new Staff(
            rs.getInt("UserId"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Password"),
            rs.getInt("StaffCardId"),
            rs.getBoolean("Admin"),
            rs.getString("Position")
        );
        s.setDeactivated(rs.getBoolean("Deactivated"));
        return s;
    }

    /** Common update of the User side (name/email/phone/password). */
    private void updateUser(User u) throws SQLException {
        updateUserPs.setString(1, u.getFirstName());
        updateUserPs.setString(2, u.getLastName());
        updateUserPs.setString(3, u.getEmail());
        updateUserPs.setString(4, u.getPhone());
        updateUserPs.setString(5, u.getPassword());
        updateUserPs.setInt(6, u.getUserId());
        updateUserPs.executeUpdate();
    }
}







