package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.dao.UserDBManager;

@WebServlet("/CustomerDataServlet")
public class CustomerDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDBManager userDBManager;

    @Override
    public void init() throws ServletException {
        // Initialize the UserDBManager with the database connection
        try {
            userDBManager = new UserDBManager(getDatabaseConnection());
        } catch (SQLException e) {
            throw new ServletException("Unable to establish database connection.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Customer> customers = userDBManager.getAllCustomers();
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("customerData.jsp").forward(request, response);
        } catch (SQLException e) {
            // Handle the error
            request.setAttribute("error", "Could not fetch customer data.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private Connection getDatabaseConnection() throws SQLException {
        // Provide your database connection details here
        String url = "jdbc:mysql://localhost:3306/your_database_name"; // Update with your database URL
        String username = "your_database_username";  // Update with your database username
        String password = "your_database_password";  // Update with your database password

        // Establish the connection and return it
        return DriverManager.getConnection(url, username, password);
    }
}
