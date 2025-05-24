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
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
        if (cartDBManager == null) {
            throw new ServletException("CartDBManager retrieved from session is null");
        }

        ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        if (applicationAccessLogDBManager == null) {
            throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
        }

        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        if (orderDBManager == null) {
            throw new ServletException("OrderDBManager retrieved from session is null");
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

                Customer customer = userDBManager.getCustomer(user.getUserId());
                int userId = customer.getUserId();
                user.setUserId(userId); 

                // Create a cart in the DB and store it in session
                java.sql.Date now = new java.sql.Date(new Date().getTime());
                int cartId = cartDBManager.addCart(new java.sql.Timestamp(now.getTime()));

                Cart cart = new Cart();
                cart.setCartId(cartId); 
                session.setAttribute("cart", cart);
                customer.setCart(cart);
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
