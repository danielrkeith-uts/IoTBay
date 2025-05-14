package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.ApplicationAccessLog;
import model.User;
import model.Enums.ApplicationAction;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.DBConnector;
import model.dao.UserDBManager;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    public static final String PAGE = "login.jsp";
    private static final String ERROR_ATTR = "loginError";

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(LoginServlet.class.getName());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Try to retrieve DB managers from session
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        ApplicationAccessLogDBManager applicationAccessLogDBManager =
            (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        // If they're not set, initialize them
        if (userDBManager == null || applicationAccessLogDBManager == null) {
            try {
                // Initialize the DB connection
                DBConnector connector = new DBConnector();
                Connection conn = connector.openConnection();

                // Initialize the managers
                userDBManager = new UserDBManager(conn);
                applicationAccessLogDBManager = new ApplicationAccessLogDBManager(conn);

                // Set them in the session for later use
                session.setAttribute("userDBManager", userDBManager);
                session.setAttribute("applicationAccessLogDBManager", applicationAccessLogDBManager);

                // Log session setup
                logger.log(Level.INFO, "UserDBManager and ApplicationAccessLogDBManager initialized and set in session.");
            } catch (Exception e) {
                throw new ServletException("Failed to initialize DB managers", e);
            }
        }

        // Get login input (email and password)
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            session.setAttribute(ERROR_ATTR, "Fill in all relevant fields");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        User user;
        try {
            // Get user details from the database
            user = userDBManager.getUser(email, password);
            if (user != null) {
                logger.log(Level.INFO, "User found: " + user.getFirstName() + " " + user.getLastName());
            } else {
                logger.log(Level.WARNING, "No user found for email: " + email);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not get user from DB", e);
            throw new ServletException("Database error while retrieving user.");
        }

        // If no user found, show error message
        if (user == null) {
            session.setAttribute(ERROR_ATTR, "Incorrect username and/or password");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        // Log the login action
        ApplicationAccessLog appAccLog = new ApplicationAccessLog(ApplicationAction.LOGIN, new Date());
        try {
            applicationAccessLogDBManager.addApplicationAccessLog(user.getUserId(), appAccLog);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not add LOGIN log", e);
            throw new ServletException("Database error while logging login.");
        }

        // Login successful, set user in session
        session.removeAttribute(ERROR_ATTR);  // Clear any previous error
        session.setAttribute("user", user);  // Store user in session
        session.setAttribute("role", user.getRole());  // Store role (if needed)

        // Redirect to a welcome page or dashboard
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}
