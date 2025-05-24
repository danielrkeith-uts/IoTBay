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

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    public static final String PAGE = "changepassword.jsp";
    private static final String ERROR_ATTR = "changePasswordError";

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(ChangePasswordServlet.class.getName());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        User user = (User) session.getAttribute("user");

        String oldPassword = request.getParameter("old-password");
        String newPassword = request.getParameter("new-password");
        String newPasswordConfirmation = request.getParameter("new-password-confirmation");

        try {
            if (!user.checkPassword(oldPassword)) {
                throw new InvalidInputException("Old password is incorrect");
            }

            if (newPassword.equals(oldPassword)) {
                throw new InvalidInputException("New password cannot be the same as your old password");
            }

            if (!Validator.isSecurePassword(newPassword)) {
                throw new InvalidInputException("Invalid password");
            }
            
            if (!newPassword.equals(newPasswordConfirmation)) {
                throw new InvalidInputException("New passwords don't match");
            }
        } catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        user.setPassword(newPassword);
        try {
            if (user instanceof Customer) {
                userDBManager.updateCustomer((Customer) user);
            } else {
                userDBManager.updateStaff((Staff) user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not update user's password");
            user.setPassword(oldPassword);
            return;
        }

        session.removeAttribute(ERROR_ATTR);
        request.getRequestDispatcher(AccountDetailsServlet.PAGE).include(request, response);
    }
}