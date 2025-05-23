package controller;

import java.io.IOException;
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
import model.Customer;
import model.Staff;
import model.User;
import model.Enums.ApplicationAction;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.UserDBManager;
import model.exceptions.InvalidInputException;
import utils.Validator;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final String PAGE = "register.jsp";
    private static final String ERROR_ATTR = "registerError";
    private static final String STAFF_PASSWORD = "staff123";
    private static final String ADMIN_PASSWORD = "admin123";

    private Logger logger;

    @Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();

    UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
    ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

    if (userDBManager == null || applicationAccessLogDBManager == null) {
        throw new ServletException("Database managers are not initialized in session.");
    }

    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String phone = request.getParameter("phone");
    String type = request.getParameter("type"); // only used for customers
    boolean isStaff = request.getParameter("isStaff") != null;
    boolean isAdmin = request.getParameter("isSystemAdmin") != null;
    String staffCardIdInput = request.getParameter("staffCardId");
    String staffPassword = request.getParameter("staffPassword");
    String adminPassword = request.getParameter("systemAdminPassword");

    try {
        User user;

        if (isStaff || isAdmin) {
            // Staff/Admin registration must have staffCardId validated
            int staffCardId = Validator.validateStaffCardId(staffCardIdInput);

            if (isAdmin) {
                if (adminPassword == null || !ADMIN_PASSWORD.equals(adminPassword)) {
                    throw new InvalidInputException("Incorrect system admin password");
                }
                user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, true);
            } else { // staff only
                if (staffPassword == null || !STAFF_PASSWORD.equals(staffPassword)) {
                    throw new InvalidInputException("Incorrect staff password");
                }
                user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, false);
            }
        } else {
            // Customer registration: Use type only if not staff/admin
            Customer.Type customerType;
            try {
                customerType = Customer.Type.valueOf(type);
            } catch (IllegalArgumentException | NullPointerException e) {
                // fallback or default type if invalid or null
                customerType = Customer.Type.INDIVIDUAL;
            }
            user = new Customer(-1, firstName, lastName, email, phone, password, customerType);
        }

        // Validate user fields (email, password strength, etc.)
        Validator.validateUser(user);

        // Check if user already exists by email
        if (userDBManager.userExists(email)) {
            throw new InvalidInputException("User already exists with that email");
        }

        // Add user to DB
        if (user instanceof Staff) {
            userDBManager.addStaff((Staff) user);
        } else {
            userDBManager.addCustomer((Customer) user);
        }

        // Log registration action
        ApplicationAccessLog log = new ApplicationAccessLog(ApplicationAction.REGISTER, new Date());
        applicationAccessLogDBManager.addApplicationAccessLog(user.getUserId(), log);

        // Set user in session and forward to welcome page
        session.setAttribute("user", user);
        session.removeAttribute(ERROR_ATTR);
        request.getRequestDispatcher("welcome.jsp").include(request, response);

    } catch (InvalidInputException | SQLException e) {
        logger.log(Level.SEVERE, "Registration error", e);
        session.setAttribute(ERROR_ATTR, e.getMessage());
        request.getRequestDispatcher(PAGE).include(request, response);
    }
}
}