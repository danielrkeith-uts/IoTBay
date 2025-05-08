package controller;

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

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(LoginServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        
        ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        if (applicationAccessLogDBManager == null) {
            throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
        }

        ApplicationAccessLog appAccLog = new ApplicationAccessLog(ApplicationAction.LOGOUT, new Date());

        try {
            applicationAccessLogDBManager.addApplicationAccessLog(user.getUserId(), appAccLog);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not add LOGOUT log");
            return;
        }

        session.removeAttribute("user");
    }
}
