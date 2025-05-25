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
import model.dao.UserDBManager;

@WebServlet("/ViewUserAccessLogsServlet")
public class ViewUserAccessLogsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ViewUserAccessLogsServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Get managers from session
        ApplicationAccessLogDBManager logDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (logDBManager == null || userDBManager == null) {
            logger.log(Level.SEVERE, "Required database managers not found in session");
            session.setAttribute("userListError", "Database connection error");
            response.sendRedirect("UserListServlet");
            return;
        }

        try {
            // Get user ID from request
            int userId = Integer.parseInt(request.getParameter("userId"));
            
            // Get user details
            User user = userDBManager.getUser(userId);
            if (user == null) {
                throw new ServletException("User not found");
            }
            
            // Get access logs for the user
            List<ApplicationAccessLog> accessLogs = logDBManager.getApplicationAccessLogs(userId);
            
            // Set attributes for the JSP
            request.setAttribute("user", user);
            request.setAttribute("accessLogs", accessLogs);
            
            // Forward to the view page
            request.getRequestDispatcher("useraccesslogs.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid user ID format", e);
            session.setAttribute("userListError", "Invalid user ID format");
            response.sendRedirect("UserListServlet");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while fetching access logs", e);
            session.setAttribute("userListError", "Error retrieving access logs");
            response.sendRedirect("UserListServlet");
        } catch (ServletException e) {
            logger.log(Level.SEVERE, "Servlet error", e);
            session.setAttribute("userListError", e.getMessage());
            response.sendRedirect("UserListServlet");
        }
    }
} 