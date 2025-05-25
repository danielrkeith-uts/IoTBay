package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Staff;
import model.User;

public class UserDBManager {
    private Connection conn;

    private static final String ADD_USER_STMT = "INSERT INTO User (FirstName, LastName, Email, Phone, Password) VALUES (?, ?, ?, ?, ?);";
    private static final String ADD_CUSTOMER_STMT  = "INSERT INTO Customer (UserId) VALUES (?);";
    private static final String ADD_STAFF_STMT = "INSERT INTO Staff (UserId, StaffCardId, Admin) VALUES (?, ?, ?);";
    private static final String GET_CUSTOMER_STMT_A = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE Email = ? AND Password = ? LIMIT 1;";
    private static final String GET_CUSTOMER_STMT_B = "SELECT * FROM User INNER JOIN Customer ON User.UserId = Customer.UserId WHERE User.UserId = ? LIMIT 1;";
    private static final String GET_STAFF_STMT_A = "SELECT * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE Email = ? AND Password = ? LIMIT 1;";
    private static final String GET_STAFF_STMT_B = "SELECT * FROM User INNER JOIN Staff ON User.UserId = Staff.UserId WHERE User.UserId = ? LIMIT 1;";
    private static final String USER_EXISTS_STMT = "SELECT 1 FROM User WHERE Email = ?";
    private static final String UPDATE_USER_STMT = "UPDATE User SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, Password = ? WHERE UserId = ?;";
    private static final String UPDATE_STAFF_STMT = "UPDATE Staff SET StaffCardId = ?, Admin = ?, Position = ? WHERE UserId = ?;";
    private static final String DELETE_USER_STMT = "DELETE FROM User WHERE UserId = ?;";
    private static final String DEACTIVATE_USER_STMT = "UPDATE User SET Deactivated = ? WHERE UserId = ?;";
    private static final String SEARCH_STAFF_STMT = "SELECT * FROM User JOIN Staff ON User.UserId = Staff.UserId WHERE (User.FirstName || ' ' || User.LastName) LIKE ? AND (Staff.Position = ? OR ? = 'ALL');";
    private static final String GET_ALL_STAFF_STMT = "SELECT * FROM User JOIN Staff ON User.UserId = Staff.UserId;";

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
    private final PreparedStatement deactivateUserPs;
    private final PreparedStatement searchStaffPs;
    private final PreparedStatement getAllStaffPs;

    private final ApplicationAccessLogDBManager applicationAccessLogDBManager;

    public UserDBManager(Connection conn) throws SQLException {
        this.conn  = conn;
        this.addUserPs = conn.prepareStatement(ADD_USER_STMT, Statement.RETURN_GENERATED_KEYS);
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
        this.deactivateUserPs = conn.prepareStatement(DEACTIVATE_USER_STMT);
        this.searchStaffPs = conn.prepareStatement(SEARCH_STAFF_STMT);
        this.getAllStaffPs = conn.prepareStatement(GET_ALL_STAFF_STMT);

        this.applicationAccessLogDBManager = new ApplicationAccessLogDBManager(conn);
    }

    public Customer getCustomer(String email, String password) throws SQLException {
        getCustomerPsA.setString(1, email);
        getCustomerPsA.setString(2, password);
        try (ResultSet rs = getCustomerPsA.executeQuery()) {
            if (!rs.next()) return null;
            Customer c = new Customer(
                rs.getInt("UserId"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("Password"),
                Customer.Type.valueOf(rs.getString("Type").toUpperCase())
            );
            c.setDeactivated(rs.getBoolean("Deactivated"));
            return c;
        }
    }

    public Customer getCustomer(int userId) throws SQLException {
        getCustomerPsB.setInt(1, userId);
        try (ResultSet rs = getCustomerPsB.executeQuery()) {
            if (!rs.next()) return null;
            Customer c = new Customer(
                rs.getInt("UserId"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("Password"),
                Customer.Type.valueOf(rs.getString("Type").toUpperCase())
            );
            c.setDeactivated(rs.getBoolean("Deactivated"));
            return c;
        }
    }

    public List<Customer> getCustomersFiltered(String name, String type) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT User.UserId, User.FirstName, User.LastName, User.Email, " +
                     "User.Phone, User.Password, User.Deactivated, Customer.Type " +
                     "FROM User JOIN Customer ON User.UserId = Customer.UserId WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            sql += " AND (LOWER(User.FirstName) LIKE ? OR LOWER(User.LastName) LIKE ?)";
            String p = "%" + name.toLowerCase() + "%";
            params.add(p); params.add(p);
        }
        if (type != null && !type.trim().isEmpty()) {
            sql += " AND Customer.Type = ?";
            params.add(type.toUpperCase());
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i+1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer(
                        rs.getInt("UserId"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Password"),
                        Customer.Type.valueOf(rs.getString("Type").toUpperCase())
                    );
                    c.setDeactivated(rs.getBoolean("Deactivated"));
                    customers.add(c);
                }
            }
        }
        return customers;
    }

    public void addCustomer(Customer customer) throws SQLException {
        int id = addUser(customer);
        addCustomerPs.setInt(1, id);
        addCustomerPs.executeUpdate();
    }

    public void addStaff(Staff staff) throws SQLException {
        int id = addUser(staff);
        addStaffPs.setInt(1, id);
        addStaffPs.setInt(2, staff.getStaffCardId());
        addStaffPs.setBoolean(3, staff.isAdmin());
        addStaffPs.executeUpdate();
    }

    public User getUser(String email, String password) throws SQLException {
        User u = getCustomer(email, password);
        return (u != null) ? u : getStaff(email, password);
    }

    public User getUser(int userId) throws SQLException {
        User u = getCustomer(userId);
        return (u != null) ? u : getStaff(userId);
    }

    public boolean userExists(String email) throws SQLException {
        userExistsPs.setString(1, email);
        try (ResultSet rs = userExistsPs.executeQuery()) {
            return rs.next();
        }
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String q = "SELECT U.UserId, U.FirstName, U.LastName, U.Email, U.Phone, U.Password, U.Deactivated, C.Type "
                 + "FROM User U JOIN Customer C ON U.UserId = C.UserId";
        try (PreparedStatement st = conn.prepareStatement(q);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer(
                    rs.getInt("UserId"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("Phone"),
                    rs.getString("Password"),
                    Customer.Type.valueOf(rs.getString("Type").toUpperCase())
                );
                c.setDeactivated(rs.getBoolean("Deactivated"));
                list.add(c);
            }
        }
        return list;
    }

    public void updateCustomer(Customer customer) throws SQLException {
        updateUserPs.setString(1, customer.getFirstName());
        updateUserPs.setString(2, customer.getLastName());
        updateUserPs.setString(3, customer.getEmail());
        updateUserPs.setString(4, customer.getPhone());
        updateUserPs.setString(5, customer.getPassword());
        updateUserPs.setInt(6, customer.getUserId());
        updateUserPs.executeUpdate();
    }

    public void updateStaff(Staff staff) throws SQLException {
        updateUserPs.setString(1, staff.getFirstName());
        updateUserPs.setString(2, staff.getLastName());
        updateUserPs.setString(3, staff.getEmail());
        updateUserPs.setString(4, staff.getPhone());
        updateUserPs.setString(5, staff.getPassword());
        updateUserPs.setInt(6, staff.getUserId());
        updateUserPs.executeUpdate();

        updateStaffPs.setInt(1, staff.getStaffCardId());
        updateStaffPs.setBoolean(2, staff.isAdmin());
        updateStaffPs.setString(3, staff.getPosition());
        updateStaffPs.setInt(4, staff.getUserId());
        updateStaffPs.executeUpdate();
    }

    public void deleteUser(int userId) throws SQLException {
        try (PreparedStatement c = conn.prepareStatement(
                "UPDATE `Order` SET OrderStatus='CANCELLED' WHERE UserId = ?")) {
            c.setInt(1, userId); c.executeUpdate();
        }
        applicationAccessLogDBManager.anonymiseApplicationAccessLogs(userId);
        deleteUserPs.setInt(1, userId);
        deleteUserPs.executeUpdate();
    }

    public void setCustomerDeactivated(int userId, boolean deactivated) throws SQLException {
        deactivateUserPs.setBoolean(1, deactivated);
        deactivateUserPs.setInt(2, userId);
        deactivateUserPs.executeUpdate();
    }

    public void deactivateStaff(int userId) throws SQLException {
        setCustomerDeactivated(userId, true);
    }

    public void reactivateStaff(int userId) throws SQLException {
        setCustomerDeactivated(userId, false);
    }

    public void reactivateCustomer(int userId) throws SQLException {
        setCustomerDeactivated(userId, false);
    }

    private int addUser(User user) throws SQLException {
        addUserPs.setString(1, user.getFirstName());
        addUserPs.setString(2, user.getLastName());
        addUserPs.setString(3, user.getEmail());
        addUserPs.setString(4, user.getPhone());
        addUserPs.setString(5, user.getPassword());
        addUserPs.executeUpdate();
        try (ResultSet rs = addUserPs.getGeneratedKeys()) {
            if (!rs.next()) throw new SQLException("Failed to insert user");
            int id = rs.getInt(1);
            user.setUserId(id);
            return id;
        }
    }

    private Staff getStaff(String email, String password) throws SQLException {
        getStaffPsA.setString(1, email);
        getStaffPsA.setString(2, password);
        try (ResultSet rs = getStaffPsA.executeQuery()) {
            if (!rs.next()) return null;
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
    }

    private Staff getStaff(int userId) throws SQLException {
        getStaffPsB.setInt(1, userId);
        try (ResultSet rs = getStaffPsB.executeQuery()) {
            if (!rs.next()) return null;
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
    }

    public List<Staff> searchStaff(String q, String position) throws SQLException {
        String namePattern = (q==null||q.trim().isEmpty()) ? "%" : "%" + q.trim() + "%";
        String posFilter   = (position==null||position.trim().isEmpty()) ? "ALL" : position.trim();

        searchStaffPs.setString(1, namePattern);
        searchStaffPs.setString(2, posFilter);
        searchStaffPs.setString(3, posFilter);

        List<Staff> list = new ArrayList<>();
        try (ResultSet rs = searchStaffPs.executeQuery()) {
            while (rs.next()) {
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
                list.add(s);
            }
        }
        return list;
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> list = new ArrayList<>();
        try (ResultSet rs = getAllStaffPs.executeQuery()) {
            while (rs.next()) {
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
                list.add(s);
            }
        }
        return list;
    }
}



