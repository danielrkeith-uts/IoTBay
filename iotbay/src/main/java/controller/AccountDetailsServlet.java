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
import model.ExtendedUser;
import model.Staff;
import model.User;
import model.dao.UserDBManager;
import model.exceptions.InvalidInputException;
import utils.Validator;

@WebServlet("/AccountDetailsServlet")
public class AccountDetailsServlet extends HttpServlet {
    public static final String PAGE = "account.jsp";
    private static final String ERROR_ATTR = "accountError";
    public static final String SUCCESS_ATTR = "accountSuccess";

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
        String customerType = request.getParameter("customerType");
        String position = request.getParameter("position");

        try {
            Validator.validatePhoneNumber(phone);

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);

            if (user instanceof Staff) {
                ((Staff) user).setPosition(position);
            }

            if (user instanceof ExtendedUser) {
                ExtendedUser extendedUser = (ExtendedUser) user;
                
                if (extendedUser.isStaff() && staffCardIdInput != null) {
                    int staffCardId = Validator.validateStaffCardId(staffCardIdInput);
                    extendedUser.setStaffCardId(staffCardId);
                }
                
                if (extendedUser.isCustomer() && customerType != null) {
                    extendedUser.setCustomerType(Customer.Type.valueOf(customerType.toUpperCase()));
                }
                
                userDBManager.updateExtendedUser(extendedUser);
            } else if (user instanceof Staff) {
                int staffCardId = Validator.validateStaffCardId(staffCardIdInput);
                ((Staff) user).setStaffCardId(staffCardId);
                userDBManager.updateStaff((Staff) user);
            } else if (user instanceof Customer) {
                if (customerType != null) {
                    ((Customer) user).setType(Customer.Type.valueOf(customerType.toUpperCase()));
                }
                userDBManager.updateCustomer((Customer) user);
            }
        } catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not update user", e);
            session.setAttribute(ERROR_ATTR, "Failed to update user details");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        session.removeAttribute(ERROR_ATTR);
        session.setAttribute(SUCCESS_ATTR, "Changes saved!");
        request.getRequestDispatcher(PAGE).include(request, response);
    }
}
