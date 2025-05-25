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

@WebServlet("/toggleUserActivation")  
public class ToggleUserActivationServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ToggleUserActivationServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session");
            request.setAttribute("error", "Database manager is not available.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        String userIdParam = request.getParameter("id");
        String deactivateParam = request.getParameter("deactivate");

        if (userIdParam == null || deactivateParam == null) {
            request.setAttribute("error", "Missing required parameters.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            boolean shouldDeactivate = Boolean.parseBoolean(deactivateParam);

            userDBManager.setCustomerDeactivated(userId, shouldDeactivate);
            
            // Redirect back to the user list
            response.sendRedirect("UserListServlet");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid user ID format", e);
            request.setAttribute("error", "Invalid user ID format.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while toggling user activation", e);
            request.setAttribute("error", "Failed to update user activation status.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
} 