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

        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session.");
            request.setAttribute("errorMessage", "Database connection error.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.log(Level.WARNING, "User not found in session.");
            request.setAttribute("errorMessage", "User session expired. Please log in again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            List<Customer> allCustomers = userDBManager.getAllCustomers();
            logger.log(Level.INFO, "Number of customers fetched: {0}", allCustomers.size());

            if (allCustomers.isEmpty()) {
                logger.log(Level.INFO, "No customers found in the database.");
                request.setAttribute("errorMessage", "No registered customers.");
            }

            request.setAttribute("customers", allCustomers);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to retrieve customers from the database", e);
            request.setAttribute("errorMessage", "Database error occurred while fetching customers.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
    }
}
