package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;
import model.Staff;
import model.User;

public class UserDBManager {
    private static final String ADD_USER_STMT = "INSERT INTO User (FirstName, LastName, Email, Phone, Password) VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_CUSTOMER_STMT = "INSERT INTO Customer (UserId) VALUES (?);";
    private static final String ADD_STAFF_STMT = "INSERT INTO Staff (UserId, StaffCardId) VALUES (?, ?);";
    private static final String GET_CUSTOMER_STMT_A = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE Email = ? AND Password = ? LIMIT 1;";
    private static final String GET_CUSTOMER_STMT_B = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE User.UserId = ? LIMIT 1;";
    private static final String GET_STAFF_STMT_A = "SELECT * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE Email = ? AND Password = ? LIMIT 1;";
    private static final String GET_STAFF_STMT_B = "SELECT * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE User.UserId = ? LIMIT 1;";
    private static final String USER_EXISTS_STMT = "SELECT 1 FROM User WHERE Email = ?";
    private static final String UPDATE_USER_STMT = "UPDATE User SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, Password = ? WHERE UserId = ?;";
    private static final String UPDATE_STAFF_STMT = "UPDATE Staff SET StaffCardId = ? WHERE UserId = ?;";
    private static final String DELETE_USER_STMT = "DELETE FROM User WHERE UserId = ?;";

    private final PreparedStatement addUserPs;
    private final PreparedStatement addCustomerPs;
    private final PreparedStatement addStaffPs;
    private final PreparedStatement getCustomerPsA;
    private final PreparedStatement getCustomerPsB;
    private final PreparedStatement getStaffPsA;
    private final PreparedStatement getStaffPsB;
    private final PreparedStatement userExistsPs;
    private final PreparedStatement updateUserPs;
    private final PreparedStatement updateStaffPs;
    private final PreparedStatement deleteUserPs;

    private final ApplicationAccessLogDBManager applicationAccessLogDBManager;

    public UserDBManager(Connection conn) throws SQLException {
        this.addUserPs = conn.prepareStatement(ADD_USER_STMT);
        this.addCustomerPs = conn.prepareStatement(ADD_CUSTOMER_STMT);
        this.addStaffPs = conn.prepareStatement(ADD_STAFF_STMT);
        this.getCustomerPsA = conn.prepareStatement(GET_CUSTOMER_STMT_A);
        this.getCustomerPsB = conn.prepareStatement(GET_CUSTOMER_STMT_B);
        this.getStaffPsA = conn.prepareStatement(GET_STAFF_STMT_A);
        this.getStaffPsB = conn.prepareStatement(GET_STAFF_STMT_B);
        this.userExistsPs = conn.prepareStatement(USER_EXISTS_STMT);
        this.updateUserPs = conn.prepareStatement(UPDATE_USER_STMT);
        this.updateStaffPs = conn.prepareStatement(UPDATE_STAFF_STMT);
        this.deleteUserPs = conn.prepareStatement(DELETE_USER_STMT);

        this.applicationAccessLogDBManager = new ApplicationAccessLogDBManager(conn);
    }

    public void addCustomer(Customer customer) throws SQLException {
        int userId = addUser(customer);

        addCustomerPs.setInt(1, userId);

        addCustomerPs.executeUpdate();
    }

    public void addStaff(Staff staff) throws SQLException {
        int userId = addUser(staff);

        addStaffPs.setInt(1, userId);
        addStaffPs.setInt(2, staff.getStaffCardId());

        addStaffPs.executeUpdate();
    }

    public User getUser(String email, String password) throws SQLException {
        User user = getCustomer(email, password);

        if (user != null) {
            return user;
        }

        return getStaff(email, password);
    }

    public User getUser(int userId) throws SQLException {
        User user = getCustomer(userId);

        if (user != null) {
            return user;
        }

        return getStaff(userId);
    }

    public boolean userExists(String email) throws SQLException {
        userExistsPs.setString(1, email);

        ResultSet rs = userExistsPs.executeQuery();

        return rs.getInt(1) == 1;
    }

    public void updateCustomer(Customer customer) throws SQLException {
        updateUser(customer);
    }

    public void updateStaff(Staff staff) throws SQLException {
        updateUser(staff);

        updateStaffPs.setInt(1, staff.getStaffCardId());
        updateStaffPs.setInt(2, staff.getUserId());

        updateStaffPs.executeUpdate();
    }

    public void deleteUser(int userId) throws SQLException {
        applicationAccessLogDBManager.anonymiseApplicationAccessLogs(userId);

        deleteUserPs.setInt(1, userId);

        deleteUserPs.executeUpdate();
    }

    private int addUser(User user) throws SQLException {
        addUserPs.setString(1, user.getFirstName());
        addUserPs.setString(2, user.getLastName());
        addUserPs.setString(3, user.getEmail());
        addUserPs.setString(4, user.getPhone());
        addUserPs.setString(5, user.getPassword());

        addUserPs.executeUpdate();
        ResultSet rs = addUserPs.getGeneratedKeys();

        if (!rs.next()) {
            throw new SQLException("Failed to insert user");
        }

        int userId = rs.getBigDecimal(1).intValue();

        user.setUserId(userId);
        return userId;
    }

    private Customer getCustomer(String email, String password) throws SQLException {
        getCustomerPsA.setString(1, email);
        getCustomerPsA.setString(2, password);

        ResultSet rs = getCustomerPsA.executeQuery();

        return toCustomer(rs);
    }

    private Customer getCustomer(int userId) throws SQLException {
        getCustomerPsB.setInt(1, userId);

        ResultSet rs = getCustomerPsB.executeQuery();

        return toCustomer(rs);
    }

    private Customer toCustomer(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new Customer(
            rs.getInt("UserId"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Password")
        );
    }

    private Staff getStaff(String email, String password) throws SQLException {
        getStaffPsA.setString(1, email);
        getStaffPsA.setString(2, password);

        ResultSet rs = getStaffPsA.executeQuery();

        return toStaff(rs);
    }

    private Staff getStaff(int userId) throws SQLException {
        getStaffPsB.setInt(1, userId);

        ResultSet rs = getStaffPsB.executeQuery();

        return toStaff(rs);
    }

    private Staff toStaff(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        return new Staff(
            rs.getInt("UserId"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Password"),
            rs.getInt("StaffCardId")
        );
    }

    private void updateUser(User user) throws SQLException {
        updateUserPs.setString(1, user.getFirstName());
        updateUserPs.setString(2, user.getLastName());
        updateUserPs.setString(3, user.getEmail());
        updateUserPs.setString(4, user.getPhone());
        updateUserPs.setString(5, user.getPassword());
        updateUserPs.setInt(6, user.getUserId());

        updateUserPs.executeUpdate();
    }
}
