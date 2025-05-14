package controller;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.dao.UserDBManager;

public class CustomerDataServlet extends HttpServlet {

    // Initialize logger
    private static final Logger logger = Logger.getLogger(CustomerDataServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the UserDBManager from the session
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        
        // Log the state of UserDBManager
        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager is null in session.");
        } else {
            logger.log(Level.INFO, "UserDBManager found in session.");
        }

        // If the UserDBManager doesn't exist in the session, throw an exception
        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager retrieved from session is null");
            throw new ServletException("UserDBManager not found in session.");
        }

        // Get customer data from the UserDBManager
        List<Customer> customers;
        try {
            customers = userDBManager.getAllCustomers();
            logger.log(Level.INFO, "Number of customers fetched: " + customers.size());
        } catch (SQLException e) {
            // Log the error if there's an issue fetching the customer data
            logger.log(Level.SEVERE, "Could not retrieve customer data", e);
            request.setAttribute("error", "Could not fetch customer data.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Set the customers as a request attribute to be accessed in the JSP
        request.setAttribute("customers", customers);

        // Forward the request to the customerData.jsp page
        request.getRequestDispatcher("customerData.jsp").forward(request, response);
    }
}
