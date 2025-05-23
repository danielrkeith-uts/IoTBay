package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import model.Enums.ApplicationAction;
import model.ApplicationAccessLog;
import model.dao.UserDBManager;
import model.dao.ApplicationAccessLogDBManager;

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
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // grab our DAOs from the session
        UserDBManager userDBManager =
            (UserDBManager) session.getAttribute("userDBManager");
        ApplicationAccessLogDBManager logMgr =
            (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        if (userDBManager == null || logMgr == null) {
            throw new ServletException("Data managers are not in session");
        }

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // simple presence check
        if (email == null || email.isEmpty()
         || password == null || password.isEmpty()) {
            session.setAttribute(ERROR_ATTR, "Please fill in both email and password.");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        User user;
        try {
            user = userDBManager.getUser(email, password);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "DB error during login", e);
            // optionally show a generic error:
            session.setAttribute(ERROR_ATTR, "Internal error; please try again.");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        // wrong credentials?
        if (user == null) {
            session.setAttribute(ERROR_ATTR, "Incorrect email or password.");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        // **NEW**: reject deactivated accounts
        if (user.isDeactivated()) {
            session.setAttribute(ERROR_ATTR,
                "Your account is currently deactivated. Contact an administrator.");
            request.getRequestDispatcher(PAGE).include(request, response);
            return;
        }

        // record login
        ApplicationAccessLog appLog =
            new ApplicationAccessLog(ApplicationAction.LOGIN, new Date());
        try {
            logMgr.addApplicationAccessLog(user.getUserId(), appLog);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to record login access log", e);
            // we won't block login if logging fails
        }

        // success!
        session.removeAttribute(ERROR_ATTR);
        session.setAttribute("user", user);
        request.getRequestDispatcher("welcome.jsp")
               .include(request, response);
    }
}
