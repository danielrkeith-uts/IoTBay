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
import model.Cart;
import model.Customer;
import model.Staff;
import model.User;
import model.Enums.ApplicationAction;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.CartDBManager;
import model.dao.OrderDBManager;
import model.dao.UserDBManager;
import model.exceptions.InvalidInputException;
import utils.Validator;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final String PAGE = "register.jsp";
    private static final String ERROR_ATTR = "registerError";
    private static final String STAFF_PASSWORD = "staff123";
    private static final String ADMIN_PASSWORD = "admin123";

    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    @Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();

    UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
    CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
    ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
    OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");

    if (userDBManager == null || cartDBManager == null || applicationAccessLogDBManager == null || orderDBManager == null) {
        throw new ServletException("One or more database managers are not initialized in session.");
    }

    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String phone = request.getParameter("phone");
    String type = request.getParameter("type"); // for customers
    boolean isStaff = request.getParameter("isStaff") != null;
    boolean isAdmin = request.getParameter("isSystemAdmin") != null;
    String staffCardIdInput = request.getParameter("staffCardId");
    String staffPassword = request.getParameter("staffPassword");
    String adminPassword = request.getParameter("systemAdminPassword");

    try {
        // Check if user already exists by email
        if (userDBManager.userExists(email)) {
            throw new InvalidInputException("User already exists with that email");
        }

        User user;

        if (isStaff || isAdmin) {
            // Validate staff card id
            int staffCardId = Validator.validateStaffCardId(staffCardIdInput);

            if (isAdmin) {
                if (adminPassword == null || !ADMIN_PASSWORD.equals(adminPassword)) {
                    throw new InvalidInputException("Incorrect system admin password");
                }
                user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, true);
            } else {
                if (staffPassword == null || !STAFF_PASSWORD.equals(staffPassword)) {
                    throw new InvalidInputException("Incorrect staff password");
                }
                user = new Staff(-1, firstName, lastName, email, phone, password, staffCardId, false);
            }
        } else {
            // Customer registration
            Customer.Type customerType;
            try {
                customerType = Customer.Type.valueOf(type);
            } catch (IllegalArgumentException | NullPointerException e) {
                customerType = Customer.Type.INDIVIDUAL; // default
            }
            user = new Customer(-1, firstName, lastName, email, phone, password, customerType);
        }

        // Validate user fields (email format, password strength, etc.)
        Validator.validateUser(user);

        // Add user to DB
        if (user instanceof Staff) {
            userDBManager.addStaff((Staff) user);
        } else {
            userDBManager.addCustomer((Customer) user);

            // Get inserted customer by email to retrieve userId
            Customer customer = userDBManager.getCustomer(email, password);
            user.setUserId(customer.getUserId());

            // Create cart and save in session
            java.sql.Date now = new java.sql.Date(new Date().getTime());
            int cartId = cartDBManager.addCart(new java.sql.Timestamp(now.getTime()));

            Cart cart = new Cart();
            cart.setCartId(cartId);
            session.setAttribute("cart", cart);
            customer.setCart(cart);
        }

        // Log registration
        ApplicationAccessLog log = new ApplicationAccessLog(ApplicationAction.REGISTER, new Date());
        applicationAccessLogDBManager.addApplicationAccessLog(user.getUserId(), log);

        // Set user session and forward
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