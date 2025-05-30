package controller;

import java.sql.SQLException;
import java.util.List;
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
import model.dao.ApplicationAccessLogDBManager;

@WebServlet("/ApplicationAccessLogUserServlet")
public class ApplicationAccessLogUserServlet extends HttpServlet {
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(LoginServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();
        ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        if (applicationAccessLogDBManager == null) {
            throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
        }

        User user = (User) session.getAttribute("user");

        List<ApplicationAccessLog> logs;
        try {
            logs = applicationAccessLogDBManager.getApplicationAccessLogs(user.getUserId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not retrieve application access logs");
            return;
        }

        user.setApplicationAccessLogs(logs);
    }
}