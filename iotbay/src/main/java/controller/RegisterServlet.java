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
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        Boolean isStaff = request.getParameter("isStaff") != null;
        String staffCardIdInput = request.getParameter("staffCardId");
        String adminPassword = request.getParameter("adminPassword");

        User user;
        try {
            if (isStaff) {
                int staffCardId;
                staffCardId = Validator.validateStaffCardId(staffCardIdInput);
                
                if (!adminPassword.equals(ADMIN_PASSWORD)) {
                    throw new InvalidInputException("Incorrect admin password");
                }

                user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId);
            } else {
                user = new Customer(-1, firstName, lastName, email, phone, password);
            }
    
            Validator.validateUser(user);
    
            boolean emailInUse;
            try {
                emailInUse = userDBManager.userExists(email);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Could not Users for in-use email in DB");
                return;
            }
    
            if (emailInUse) {
                throw new InvalidInputException("User already exists with that email");
            }
        } catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }
        
        try {
            if (isStaff) {
                userDBManager.addStaff((Staff) user);
            } else {        
                userDBManager.addCustomer((Customer) user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not add user into DB");
            return;
        }

        session.setAttribute("user", user);
        session.removeAttribute(ERROR_ATTR);
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}
