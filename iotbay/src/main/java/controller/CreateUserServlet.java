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
import model.dao.UserDBManager;
import model.exceptions.InvalidInputException;
import utils.Validator;

@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(CreateUserServlet.class.getName());
    private static final String PAGE = "createuser.jsp";
    private static final String ERROR_ATTR = "createUserError";
    private static final String STAFF_PASSWORD = "staff123";
    private static final String ADMIN_PASSWORD = "admin123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the create user page
        request.getRequestDispatcher(PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session");
            session.setAttribute(ERROR_ATTR, "Database manager is not available.");
            response.sendRedirect(PAGE);
            return;
        }

        try {
            // Get form data
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String isStaffStr = request.getParameter("isStaff");
            String isCustomerStr = request.getParameter("isCustomer");
            String isAdminStr = request.getParameter("isAdmin");
            String staffCardIdStr = request.getParameter("staffCardId");
            String customerType = request.getParameter("customerType");
            String staffPassword = request.getParameter("staffPassword");
            String adminPassword = request.getParameter("systemAdminPassword");

            // Validate input
            Validator.validateName(firstName, "First Name");
            Validator.validateName(lastName, "Last Name");
            Validator.validatePhoneNumber(phone);
            
            if (!password.equals(confirmPassword)) {
                throw new InvalidInputException("Passwords do not match");
            }

            // Create a temporary user to validate email and password format
            Customer tempUser = new Customer(-1, firstName, lastName, email, phone, password, Customer.Type.INDIVIDUAL);
            Validator.validateUser(tempUser);

            // Check if email already exists
            if (userDBManager.userExists(email)) {
                throw new InvalidInputException("Email already exists");
            }

            boolean isStaff = "true".equals(isStaffStr);
            boolean isCustomer = "true".equals(isCustomerStr);
            boolean isAdmin = "true".equals(isAdminStr);

            // Validate that at least one role is selected
            if (!isStaff && !isCustomer) {
                throw new InvalidInputException("Please select at least one role (Staff or Customer)");
            }

            // Validate staff-specific fields
            if (isStaff) {
                if (staffCardIdStr == null || staffCardIdStr.trim().isEmpty()) {
                    throw new InvalidInputException("Staff Card ID is required for staff members");
                }
                int staffCardId = Validator.validateStaffCardId(staffCardIdStr);

                // Validate staff password
                if (isAdmin) {
                    if (!ADMIN_PASSWORD.equals(adminPassword)) {
                        throw new InvalidInputException("Invalid system admin password");
                    }
                } else if (!STAFF_PASSWORD.equals(staffPassword)) {
                    throw new InvalidInputException("Invalid staff password");
                }

                // Create staff user
                Staff staff = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, isAdmin, "");
                userDBManager.addStaff(staff);
            }

            // Create customer if selected
            if (isCustomer) {
                if (customerType == null || customerType.trim().isEmpty()) {
                    throw new InvalidInputException("Customer type is required for customers");
                }
                Customer customer = new Customer(-1, firstName, lastName, email, phone, password, 
                    Customer.Type.valueOf(customerType.toUpperCase()));
                userDBManager.addCustomer(customer);
            }

            // Redirect to user list with success message
            session.setAttribute("editUserSuccess", "User created successfully");
            response.sendRedirect("UserListServlet");

        } catch (InvalidInputException e) {
            logger.log(Level.WARNING, "Invalid input: {0}", e.getMessage());
            session.setAttribute(ERROR_ATTR, e.getMessage());
            response.sendRedirect(PAGE);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while creating user", e);
            session.setAttribute(ERROR_ATTR, "Error creating user");
            response.sendRedirect(PAGE);
        }
    }
} 