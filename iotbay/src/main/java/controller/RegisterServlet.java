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
    public static final String PAGE = "register.jsp";
    private static final String ERROR_ATTR = "registerError";
    private static final String ADMIN_PASSWORD = "admin";

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(RegisterServlet.class.getName());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        ApplicationAccessLogDBManager logDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        if (userDBManager == null || logDBManager == null) {
            throw new ServletException("Database managers are not set in session.");
        }

        // Get form data
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone").replaceAll("\\s+", "");
        boolean isStaff = request.getParameter("isStaff") != null;
        String staffCardIdInput = request.getParameter("staffCardId");
        String adminPassword = request.getParameter("adminPassword");

        // Validate user input
        try {
            if (!Validator.isSecurePassword(password)) {
                throw new InvalidInputException("Password must include an uppercase & lowercase letter, number, special character, and be 8 characters long");
            }

            if (!phone.isEmpty() && !Validator.isPhoneNumber(phone)) {
                throw new InvalidInputException("Invalid phone number");
            }

            if (userDBManager.userExists(email)) {
                throw new InvalidInputException("User already exists with that email");
            }

            User user;
            if (isStaff) {
                int staffCardId = Validator.validateStaffCardId(staffCardIdInput);

                if (!adminPassword.equals(ADMIN_PASSWORD)) {
                    throw new InvalidInputException("Incorrect admin password");
                }

                user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId);
                userDBManager.addStaff((Staff) user);
            } else {
                user = new Customer(-1, firstName, lastName, email, phone, password);
                userDBManager.addCustomer((Customer) user);
            }

            // Log registration
            ApplicationAccessLog log = new ApplicationAccessLog(ApplicationAction.REGISTER, new Date());
            logDBManager.addApplicationAccessLog(user.getUserId(), log);

            session.setAttribute("user", user);
            session.removeAttribute(ERROR_ATTR);
            request.getRequestDispatcher("welcome.jsp").include(request, response);

        } catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during registration", e);
            session.setAttribute(ERROR_ATTR, "A server error occurred. Please try again.");
            request.getRequestDispatcher(PAGE).include(request, response);
        }
    }
}
