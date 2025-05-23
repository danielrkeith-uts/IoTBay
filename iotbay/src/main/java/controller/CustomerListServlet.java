package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.log(Level.WARNING, "Session not found. Redirecting to login.");
            request.setAttribute("errorMessage", "Session expired or not found. Please log in.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session.");
            request.setAttribute("errorMessage", "Database connection error.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.log(Level.WARNING, "User session expired. Redirecting to login.");
            request.setAttribute("errorMessage", "User session expired. Please log in again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        String searchName = request.getParameter("searchName");
        String searchType = request.getParameter("searchType");

        try {
            List<Customer> customers;

            if ((searchName == null || searchName.trim().isEmpty()) && (searchType == null || searchType.trim().isEmpty())) {
                customers = userDBManager.getAllCustomers();
            } else {
                customers = userDBManager.getCustomersFiltered(searchName, searchType);
            }

            if (customers == null) {
                customers = new ArrayList<>();
            }

            logger.log(Level.INFO, "Number of customers fetched: {0}", customers.size());

            request.setAttribute("customers", customers);
            request.getRequestDispatcher("customerlist.jsp").forward(request, response);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to retrieve customers from the database", e);
            request.setAttribute("errorMessage", "Database error occurred while fetching customers.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
