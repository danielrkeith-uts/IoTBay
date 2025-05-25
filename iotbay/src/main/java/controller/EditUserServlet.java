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

@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
    private static final String PAGE = "edituser.jsp";
    private static final String ERROR_ATTR = "editUserError";
    private static final String SUCCESS_ATTR = "editUserSuccess";
    private static final String STAFF_PASSWORD = "staff123";
    private static final String ADMIN_PASSWORD = "admin123";
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(EditUserServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            session.setAttribute(ERROR_ATTR, "No user ID provided");
            response.sendRedirect("userlist.jsp");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            ExtendedUser user = userDBManager.getAllUsersFilteredByName("").stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);

            if (user == null) {
                session.setAttribute(ERROR_ATTR, "User not found");
                response.sendRedirect("userlist.jsp");
                return;
            }

            request.setAttribute("editUser", user);
            request.getRequestDispatcher(PAGE).forward(request, response);

        } catch (NumberFormatException e) {
            session.setAttribute(ERROR_ATTR, "Invalid user ID");
            response.sendRedirect("userlist.jsp");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving user", e);
            session.setAttribute(ERROR_ATTR, "Error retrieving user details");
            response.sendRedirect("userlist.jsp");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        String userIdStr = request.getParameter("userId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String staffCardIdInput = request.getParameter("staffCardId");
        String customerType = request.getParameter("customerType");
        String isStaffStr = request.getParameter("isStaff");
        String isCustomerStr = request.getParameter("isCustomer");
        String isAdminStr = request.getParameter("isAdmin");
        String staffPassword = request.getParameter("staffPassword");
        String adminPassword = request.getParameter("systemAdminPassword");

        try {
            int userId = Integer.parseInt(userIdStr);
            ExtendedUser user = userDBManager.getAllUsersFilteredByName("").stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("User not found"));

            // Get the original staff/admin status
            boolean wasStaff = user.isStaff();
            boolean wasAdmin = user.isAdmin();

            // Validate and update basic user information
            Validator.validatePhoneNumber(phone);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhone(phone);

            // Update staff properties
            boolean isStaff = "true".equalsIgnoreCase(isStaffStr);
            boolean isAdmin = "true".equalsIgnoreCase(isAdminStr);

            // If becoming staff or admin, validate passwords
            if (isStaff && !wasStaff) {
                if (staffPassword == null || !STAFF_PASSWORD.equals(staffPassword)) {
                    throw new InvalidInputException("Incorrect staff password");
                }
            }
            if (isAdmin && !wasAdmin) {
                if (adminPassword == null || !ADMIN_PASSWORD.equals(adminPassword)) {
                    throw new InvalidInputException("Incorrect system admin password");
                }
            }

            user.setStaff(isStaff);
            if (isStaff && staffCardIdInput != null && !staffCardIdInput.trim().isEmpty()) {
                int staffCardId = Validator.validateStaffCardId(staffCardIdInput);
                user.setStaffCardId(staffCardId);
                user.setAdmin(isAdmin);
            }

            // Update customer properties
            boolean isCustomer = "true".equalsIgnoreCase(isCustomerStr);
            user.setCustomer(isCustomer);
            if (isCustomer && customerType != null && !customerType.trim().isEmpty()) {
                user.setCustomerType(Customer.Type.valueOf(customerType.toUpperCase()));
            }

            userDBManager.updateExtendedUser(user);
            session.setAttribute(SUCCESS_ATTR, "User updated successfully");
            response.sendRedirect("UserListServlet");

        } catch (NumberFormatException e) {
            session.setAttribute(ERROR_ATTR, "Invalid user ID");
            request.getRequestDispatcher(PAGE).forward(request, response);
        } catch (InvalidInputException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user", e);
            session.setAttribute(ERROR_ATTR, "Failed to update user");
            request.getRequestDispatcher(PAGE).forward(request, response);
        }
    }
} 