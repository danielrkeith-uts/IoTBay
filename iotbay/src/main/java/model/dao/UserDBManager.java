package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;

public class UserDBManager {
    private static final String GET_CUSTOMER_QUERY = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE Email = ? AND Password = ? LIMIT 1";

    private final PreparedStatement getCustomerPs;

    public UserDBManager(Connection conn) throws SQLException {
        this.getCustomerPs = conn.prepareStatement(GET_CUSTOMER_QUERY);
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
}
