package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;
import model.Staff;

public class UserDBManager {
    private static final String GET_CUSTOMER_QUERY = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE Email = ? AND Password = ? LIMIT 1";
    private static final String GET_STAFF_QUERY = "SELECT * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE Email = ? AND Password = ? LIMIT 1";

    private final PreparedStatement getCustomerPs;
    private final PreparedStatement getStaffPs;

    public UserDBManager(Connection conn) throws SQLException {
        this.getCustomerPs = conn.prepareStatement(GET_CUSTOMER_QUERY);
        this.getStaffPs = conn.prepareStatement(GET_STAFF_QUERY);
    }

    public Customer getCustomer(String email, String password) throws SQLException {
        getCustomerPs.setString(1, email);
        getCustomerPs.setString(2, password);

        ResultSet rs = getCustomerPs.executeQuery();

        if (!rs.next()) {
            return null;
        }

        return new Customer(
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Password")
        );
    }

    public Staff getStaff(String email, String password) throws SQLException {
        getStaffPs.setString(1, email);
        getStaffPs.setString(2, password);

        ResultSet rs = getStaffPs.executeQuery();

        if (!rs.next()) {
            return null;
        }

        return new Staff(
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Password"),
            rs.getInt("StaffCardId")
        );
    }
}
