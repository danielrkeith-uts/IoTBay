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
    public void init() {
        logger = Logger.getLogger(RegisterServlet.class.getName());
    }

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
        String type = request.getParameter("type");
        boolean isStaff = request.getParameter("isStaff") != null;
        boolean isAdmin = request.getParameter("isSystemAdmin") != null;
        String staffCardIdInput = request.getParameter("staffCardId");
        String staffPassword = request.getParameter("staffPassword");
        String adminPassword = request.getParameter("systemAdminPassword");

        try {
            User user;

            if (isStaff || isAdmin) {
                int staffCardId = Validator.validateStaffCardId(staffCardIdInput);

                if (isAdmin) {
                    if (!ADMIN_PASSWORD.equals(adminPassword)) {
                        throw new InvalidInputException("Incorrect system admin password");
                    }
                    user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, true);
                } else {
                    if (!STAFF_PASSWORD.equals(staffPassword)) {
                        throw new InvalidInputException("Incorrect staff password");
                    }
                    user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, false);
                }

            } else {
                user = new Customer(-1, firstName, lastName, email, phone, password, Customer.Type.valueOf(type));
            }

            Validator.validateUser(user);

            if (userDBManager.userExists(email)) {
                throw new InvalidInputException("User already exists with that email");
            }

            if (user instanceof Staff) {
                userDBManager.addStaff((Staff) user);
            } else {
                userDBManager.addCustomer((Customer) user);
            }

            ApplicationAccessLog log = new ApplicationAccessLog(ApplicationAction.REGISTER, new Date());
            applicationAccessLogDBManager.addApplicationAccessLog(user.getUserId(), log);

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
