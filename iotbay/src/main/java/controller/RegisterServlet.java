package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.Staff;
import model.User;
import model.dao.UserDBManager;
import utils.Validator;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    public static final String PAGE = "register.jsp";
    private static final String ERROR_ATTR = "registerError";
    private static final String ADMIN_PASSWORD = "admin";

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(LoginServlet.class.getName());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        String email = request.getParameter("email");
        if (!Validator.isEmail(email)) {
            session.setAttribute(ERROR_ATTR, "Invalid email");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        boolean emailInUse;
        try {
            emailInUse = userDBManager.userExists(email);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not Users for in-use email in DB");
            return;
        }

        if (emailInUse) {
            session.setAttribute(ERROR_ATTR, "User already exists with that email");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        String password = request.getParameter("password");
        if (!Validator.isSecurePassword(password)) {
            session.setAttribute(ERROR_ATTR, "Password must include an uppercase & lowercase letter, number, special character, and be 8 characters long");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        String phone = request.getParameter("phone").replaceAll("\\s+", "");
        if (!phone.isEmpty() && !Validator.isPhoneNumber(phone)) {
            session.setAttribute(ERROR_ATTR, "Invalid phone number");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        boolean isStaff = request.getParameter("isStaff") != null;

        User user;
        if (isStaff) {
            String staffCardIdInput = request.getParameter("staffCardId");
            int staffCardId;
            try {
                if (staffCardIdInput.isEmpty()) {
                    throw new NumberFormatException();
                }

                staffCardId = Integer.parseInt(staffCardIdInput);
            } catch (NumberFormatException e) {
                session.setAttribute(ERROR_ATTR, "Invalid Staff Card ID");
                request.getRequestDispatcher(PAGE).include(request, response);
                return;
            }

            String adminPassword = request.getParameter("adminPassword");
            if (!adminPassword.equals(ADMIN_PASSWORD)) {
                session.setAttribute(ERROR_ATTR, "Incorrect admin password");
                request.getRequestDispatcher(PAGE).include(request, response);
                return;
            }

            Staff staff = new Staff(-1, firstName, lastName, email, phone, password, staffCardId);

            try {
                userDBManager.addStaff(staff);;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Could not add staff into DB");
                return;
            }

            user = staff;
        } else {
            Customer customer = new Customer(-1, firstName, lastName, email, phone, password);
        
            try {
                userDBManager.addCustomer(customer);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Could not add customer into DB");
                return;
            }

            user = customer;
        }

        session.setAttribute("user", user);
        session.removeAttribute(ERROR_ATTR);
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}
