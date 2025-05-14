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

    User user = (User) session.getAttribute("user");
    if (user == null) {
        logger.severe("User object is missing in session.");
        response.sendRedirect("login.jsp");  
        return;
    }

    UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
    if (userDBManager == null) {
        logger.severe("UserDBManager is missing in session.");
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UserDBManager not found in session.");
        return;
    }

    logger.info("User object found in session: " + user);

    List<Customer> customers;
    try {
        customers = userDBManager.getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/customerlist.jsp").forward(request, response);
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Failed to retrieve customers", e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve customers from the database");
    }
}
}