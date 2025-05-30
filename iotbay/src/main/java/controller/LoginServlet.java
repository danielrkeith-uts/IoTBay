package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Cart;
import model.Customer;
import model.ProductListEntry;
import model.User;
import model.Enums.ApplicationAction;
import model.ApplicationAccessLog;
import model.dao.UserDBManager;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.ProductListEntryDBManager;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    public static final String PAGE       = "login.jsp";
    private static final String ERROR_ATTR = "loginError";
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(LoginServlet.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward GET requests to the login page
        request.getRequestDispatcher(PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.getRequestDispatcher("/ConnServlet").include(request, response);
        UserDBManager userDBManager =
            (UserDBManager) session.getAttribute("userDBManager");
        ApplicationAccessLogDBManager logMgr =
            (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        if (userDBManager == null || logMgr == null) {
            throw new ServletException("Data managers are not in session");
        }

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.isEmpty()
         || password == null || password.isEmpty()) {
            session.setAttribute(ERROR_ATTR, "Please fill in both email and password.");
            request.getRequestDispatcher(PAGE).forward(request, response);
            return;
        }

        User user;
        try {
            user = userDBManager.getUser(email, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "DB error during login", e);
            session.setAttribute(ERROR_ATTR, "Internal error; please try again.");
            request.getRequestDispatcher(PAGE).forward(request, response);
            return;
        }

        if (user == null) {
            session.setAttribute(ERROR_ATTR, "Invalid email or password");
            request.getRequestDispatcher(PAGE).forward(request, response);
            return;
        }
        
        if (user.isDeactivated()) {
            session.setAttribute("deactivatedUser", user);
            session.setAttribute(ERROR_ATTR, "Your account is deactivated. Please contact an administrator.");
            request.getRequestDispatcher(PAGE).forward(request, response);
            return;
        }
        ApplicationAccessLog appLog =
            new ApplicationAccessLog(ApplicationAction.LOGIN, new Date());
        try {
            logMgr.addApplicationAccessLog(user.getUserId(), appLog);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to record login access log", e);
        }

        session.removeAttribute(ERROR_ATTR);
        session.setAttribute("user", user);

        Cart sessionCart = (Cart) session.getAttribute("cart");
        if (sessionCart != null && user instanceof Customer) {
            ProductListEntryDBManager productListEntryDBManager =
                (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
            if (productListEntryDBManager == null) {
                throw new ServletException("ProductListEntryDBManager missing from session");
            }
            Customer customer = (Customer) user;
            Cart dbCart = customer.getCart();
            for (ProductListEntry entry : sessionCart.getProductList()) {
                dbCart.addProduct(entry.getProduct(), entry.getQuantity());
                try {
                    productListEntryDBManager.addProduct(
                        user.getUserId(),
                        entry.getProduct().getProductId(),
                        entry.getQuantity()
                    );
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Failed to merge cart item to DB", e);
                }
            }
            session.removeAttribute("cart");
        }
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}