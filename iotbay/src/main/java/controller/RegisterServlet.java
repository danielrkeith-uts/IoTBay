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
import model.User;
import model.dao.UserDBManager;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
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

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        Customer newCustomer = new Customer(-1, firstName, lastName, email, phone, password);

        // TODO - error handling, and add verification that no other user exists with this email/password
    
        try {
            userDBManager.addCustomer(newCustomer);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not add user into DB");
            return;
        }

        User user;
        try {
            user = userDBManager.getUser(email, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not retrieve registered user from DB");
            return;
        }

        session.setAttribute("user", user);
        request.getRequestDispatcher("welcome.jsp").include(request, response);
    }
}
