package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        ApplicationAccessLogDBManager logMgr = (ApplicationAccessLogDBManager)
                                               session.getAttribute("applicationAccessLogDBManager");

        if (userDBManager == null || logMgr == null) {
            throw new ServletException("Missing DB managers in session");
        }

        String email         = request.getParameter("email");
        String password      = request.getParameter("password");
        String firstName     = request.getParameter("firstName");
        String lastName      = request.getParameter("lastName");
        String phone         = request.getParameter("phone");
        boolean isStaff      = request.getParameter("isStaff") != null;
        String staffCardRaw  = request.getParameter("staffCardId");
        String adminPassword = request.getParameter("adminPassword");
        String position      = request.getParameter("position");          // new field
        if (position == null || position.trim().isEmpty()) {
            position = "STAFF";
        }

        User user;
        try {
            if (isStaff) {
                // validate staff‐card
                int staffCardId = Validator.validateStaffCardId(staffCardRaw);

                // only allow staff creation with correct admin‐pass
                boolean makeAdmin = false;
                if (ADMIN_PASSWORD.equals(adminPassword)) {
                    makeAdmin = true;
                }

                // now create Staff with the new 8‐arg constructor
                user = new Staff(
                    -1,
                    firstName, lastName, email, phone, password,
                    staffCardId,
                    makeAdmin,
                    position
                );
            } else {
                user = new Customer(
                    -1,
                    firstName, lastName, email, phone, password
                );
            }

            // validate common fields (email/password/phone)
            Validator.validateUser(user);

            // ensure email not already used
            if (userDBManager.userExists(email)) {
                throw new InvalidInputException("Email already in use");
            }
        }
        catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }
        catch (SQLException sqle) {
            logger.log(Level.SEVERE, "Error checking userExists", sqle);
            throw new ServletException(sqle);
        }

        // persisted
        try {
            if (user instanceof Staff) {
                userDBManager.addStaff((Staff) user);
            } else {
                userDBManager.addCustomer((Customer) user);
            }
        } catch (SQLException sqle) {
            logger.log(Level.SEVERE, "Error inserting new user", sqle);
            throw new ServletException(sqle);
        }

        // log registration
        ApplicationAccessLog appLog = new ApplicationAccessLog(
            ApplicationAction.REGISTER, new Date()
        );
        try {
            logMgr.addApplicationAccessLog(user.getUserId(), appLog);
        } catch (SQLException sqle) {
            logger.log(Level.WARNING, "Could not log registration", sqle);
        }

        // ready to go
        session.setAttribute("user", user);
        session.removeAttribute(ERROR_ATTR);
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}
