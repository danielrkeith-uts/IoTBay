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

@WebServlet("/AccountDetailsServlet")
public class AccountDetailsServlet extends HttpServlet {
    public static final String PAGE = "account.jsp";
    private static final String ERROR_ATTR = "accountError";
    private static final String SUCCESS_ATTR = "accountSuccess";

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(AccountDetailsServlet.class.getName());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        User user = (User) session.getAttribute("user");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String staffCardIdInput = request.getParameter("staffCardId");

        try {
            Validator.validatePhoneNumber(phone);

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);

            if (user instanceof Staff) {
                int staffCardId = Validator.validateStaffCardId(staffCardIdInput);
                
                ((Staff) user).setStaffCardId(staffCardId);
            }
        } catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        try {
            if (user instanceof Customer) {
                userDBManager.updateCustomer((Customer) user);
            } else {
                userDBManager.updateStaff((Staff) user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not update user");
            return;
        }

        session.removeAttribute(ERROR_ATTR);
        session.setAttribute(SUCCESS_ATTR, "Changes saved!");
        request.getRequestDispatcher(PAGE).include(request, response);
    }
}
