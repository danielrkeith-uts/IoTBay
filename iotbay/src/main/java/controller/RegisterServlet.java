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
import model.dao.UserDBManager;
import utils.Validator;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    public static final String PAGE = "register.jsp";
    private static final String ERROR_ATTR = "registerError";

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

        Customer customer = new Customer(-1, firstName, lastName, email, phone, password);
        
        try {
            userDBManager.addCustomer(customer);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not add user into DB");
            return;
        }

        session.setAttribute("user", customer);
        session.removeAttribute(ERROR_ATTR);
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}
