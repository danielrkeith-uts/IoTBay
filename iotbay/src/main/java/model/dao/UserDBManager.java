package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;
import model.Staff;
import model.User;

public class UserDBManager {
    private static final String GET_CUSTOMER_STMT_A = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE Email = ? AND Password = ? LIMIT 1";
    private static final String GET_CUSTOMER_STMT_B = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE User.UserId = ? LIMIT 1";
    private static final String GET_STAFF_STMT_A = "SELECT * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE Email = ? AND Password = ? LIMIT 1";
    private static final String GET_STAFF_STMT_B = "Select * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE User.UserId = ? LIMIT 1";
    private static final String DELETE_USER_STMT = "DELETE FROM User WHERE UserId = ?";

    private final PreparedStatement getCustomerPsA;
    private final PreparedStatement getCustomerPsB;
    private final PreparedStatement getStaffPsA;
    private final PreparedStatement getStaffPsB;
    private final PreparedStatement deleteUserPs;

    public UserDBManager(Connection conn) throws SQLException {
        this.getCustomerPsA = conn.prepareStatement(GET_CUSTOMER_STMT_A);
        this.getCustomerPsB = conn.prepareStatement(GET_CUSTOMER_STMT_B);
        this.getStaffPsA = conn.prepareStatement(GET_STAFF_STMT_A);
        this.getStaffPsB = conn.prepareStatement(GET_STAFF_STMT_B);
        this.deleteUserPs = conn.prepareStatement(DELETE_USER_STMT);
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

    public void deleteUser(int userId) throws SQLException {
        deleteUserPs.setInt(1, userId);

        deleteUserPs.executeUpdate();
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
}
