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
    private static final String PAGE = "register.jsp";
    private static final String ERROR_ATTR = "registerError";
    private static final String STAFF_PASSWORD = "staff123";
    private static final String ADMIN_PASSWORD = "admin123";

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(RegisterServlet.class.getName());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager =
            (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        CartDBManager cartDBManager =
            (CartDBManager) session.getAttribute("cartDBManager");
        if (cartDBManager == null) {
            throw new ServletException("CartDBManager retrieved from session is null");
        }

        ApplicationAccessLogDBManager applicationAccessLogDBManager =
            (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        if (applicationAccessLogDBManager == null) {
            throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
        }

        OrderDBManager orderDBManager =
            (OrderDBManager) session.getAttribute("orderDBManager");
        if (orderDBManager == null) {
            throw new ServletException("OrderDBManager retrieved from session is null");
        }

        
        String email    = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName= request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone    = request.getParameter("phone");
        String type     = request.getParameter("type"); //customer
        boolean isStaff = request.getParameter("isStaff") != null;
        boolean isAdmin = request.getParameter("isSystemAdmin") != null;
        String staffCardIdInput    = request.getParameter("staffCardId");
        String staffPassword       = request.getParameter("staffPassword");
        String adminPassword       = request.getParameter("systemAdminPassword");
        String position            = request.getParameter("position"); //staff

        User user = null; 

        try {
            if (userDBManager.userExists(email)) {
                throw new InvalidInputException("User already exists with that email");
            }

            if (isStaff || isAdmin) {
                // staff path
                int staffCardId = Validator.validateStaffCardId(staffCardIdInput);

                if (isAdmin) {
                    if (adminPassword == null || !ADMIN_PASSWORD.equals(adminPassword)) {
                        throw new InvalidInputException("Incorrect system admin password");
                    }
                    user = new Staff(
                        -1, firstName, lastName, email, phone, password,
                        staffCardId, true, position
                    );
                } else {
                    if (staffPassword == null || !STAFF_PASSWORD.equals(staffPassword)) {
                        throw new InvalidInputException("Incorrect staff password");
                    }
                    user = new Staff(
                        -1, firstName, lastName, email, phone, password,
                        staffCardId, false, position
                    );
                }
            } else {
                
                Customer.Type customerType;
                try {
                    customerType = Customer.Type.valueOf(type);
                } catch (IllegalArgumentException | NullPointerException e) {
                    customerType = Customer.Type.INDIVIDUAL;
                }
                user = new Customer(
                    -1, firstName, lastName, email, phone, password, customerType
                );
            }

            Validator.validateUser(user);

            if (user instanceof Staff) {
                userDBManager.addStaff((Staff) user);
            } else {
                userDBManager.addCustomer((Customer) user);
                Customer customer = userDBManager.getCustomer(email, password);
                user.setUserId(customer.getUserId());

                java.sql.Date now = new java.sql.Date(new Date().getTime());
                int cartId = cartDBManager.addCart(new java.sql.Timestamp(now.getTime()));
                Cart cart = new Cart();
                cart.setCartId(cartId);
                session.setAttribute("cart", cart);
                customer.setCart(cart);
            }

            
            ApplicationAccessLog appLog =
                new ApplicationAccessLog(ApplicationAction.REGISTER, new Date());
            applicationAccessLogDBManager
                .addApplicationAccessLog(user.getUserId(), appLog);

            session.setAttribute("user", user);
            session.removeAttribute(ERROR_ATTR);
            request.getRequestDispatcher("welcome.jsp")
                   .include(request, response);

        } catch (InvalidInputException | SQLException e) {
            logger.log(Level.SEVERE, "Registration error", e);
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
        }
    }
}
