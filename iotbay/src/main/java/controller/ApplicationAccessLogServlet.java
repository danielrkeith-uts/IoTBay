package controller;

import java.io.IOException;
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

@WebServlet("/ApplicationAccessLogServlet")
public class ApplicationAccessLogServlet extends HttpServlet {
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(ApplicationAccessLogServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        
        if (applicationAccessLogDBManager == null) {
            throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<ApplicationAccessLog> logs = null;

        try {
            if ("admin".equals(user.getRole())) {
                String customerIdParam = request.getParameter("customer_id");
                
                if (customerIdParam != null) {
                    int customerId = Integer.parseInt(customerIdParam);
                    logs = applicationAccessLogDBManager.getApplicationAccessLogs(customerId);
                } else {
                    response.sendRedirect("error.jsp"); 
                    return;
                }
            } else {
                logs = applicationAccessLogDBManager.getApplicationAccessLogs(user.getUserId());
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not retrieve application access logs", e);
            response.sendRedirect("error.jsp");
            return;
        }

        request.setAttribute("accessLogs", logs);
        request.getRequestDispatcher("/viewaccesslogs.jsp").forward(request, response);
    }
}
