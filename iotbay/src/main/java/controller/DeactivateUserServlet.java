package controller;

import model.dao.UserDBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DeactivateUserServlet")
public class DeactivateUserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DeactivateUserServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session");
            session.setAttribute("userListError", "Database manager is not available.");
            response.sendRedirect("UserListServlet");
            return;
        }

        String userIdStr = request.getParameter("userId");
        String action = request.getParameter("action");

        if (userIdStr == null || action == null) {
            session.setAttribute("userListError", "Missing required parameters.");
            response.sendRedirect("UserListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            boolean shouldDeactivate = "deactivate".equals(action);

            userDBManager.setCustomerDeactivated(userId, shouldDeactivate);
            
            // Set success message
            session.setAttribute("editUserSuccess", 
                shouldDeactivate ? "User has been deactivated." : "User has been activated.");
            
            // Redirect back to the user list
            response.sendRedirect("UserListServlet");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid user ID format", e);
            session.setAttribute("userListError", "Invalid user ID format.");
            response.sendRedirect("UserListServlet");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while toggling user activation", e);
            session.setAttribute("userListError", "Failed to update user activation status.");
            response.sendRedirect("UserListServlet");
        }
    }
} 