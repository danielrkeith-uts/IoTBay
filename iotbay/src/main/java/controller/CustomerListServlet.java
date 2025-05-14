package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.User;
import model.dao.UserDBManager;

@WebServlet("/CustomerListServlet")
public class CustomerListServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CustomerListServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();

        // Retrieve UserDBManager from session
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        // Check if UserDBManager is null
        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UserDBManager not found in session.");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.log(Level.SEVERE, "User not found in session.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User not found in session.");
            return;
        }

        List<Customer> allCustomers = null;
        try {
            // Fetch the list of customers from the database
            allCustomers = userDBManager.getAllCustomers();

            // Debug: log the number of customers fetched
            logger.log(Level.INFO, "Number of customers fetched: " + (allCustomers != null ? allCustomers.size() : 0));

            // Check if no customers were found
            if (allCustomers == null || allCustomers.isEmpty()) {
                logger.log(Level.INFO, "No customers found in the database.");
            }

            // Add the list of customers to the request attributes so they can be accessed in the JSP
            request.setAttribute("customers", allCustomers);

            // Forward the request to a JSP page for displaying the customer list
            request.getRequestDispatcher("/customerlist.jsp").forward(request, response);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to retrieve customers from the database", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve customers from the database");
        }
    }
}
