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
import model.User;
import model.Enums.ApplicationAction;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.UserDBManager;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    public static final String PAGE = "login.jsp";
    private static final String ERROR_ATTR = "loginError";

    private Logger logger;

    @Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();

    UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
    if (userDBManager == null) {
        throw new ServletException("UserDBManager retrieved from session is null");
    }

    ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
    if (applicationAccessLogDBManager == null) {
        throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
    }

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    if (email.isEmpty() || password.isEmpty()) {
        session.setAttribute(ERROR_ATTR, "Fill in all relevant fields");
        request.getRequestDispatcher(PAGE).include(request, response);
        return;
    }

    User user;
    try {
        user = userDBManager.getUser(email, password);
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Could not get user from DB");
        return;
    }

    if (user == null) {
        session.setAttribute(ERROR_ATTR, "Incorrect username and/or password");
        request.getRequestDispatcher(PAGE).include(request, response);
        return;
    }

    ApplicationAccessLog appAccLog = new ApplicationAccessLog(ApplicationAction.LOGIN, new Date());

    try {
        applicationAccessLogDBManager.addApplicationAccessLog(user.getUserId(), appAccLog);
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Could not add LOGIN log");
        return;
    }

    session.removeAttribute(ERROR_ATTR);
    session.setAttribute("user", user);

    response.sendRedirect("welcome.jsp");  
}
}