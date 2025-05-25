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
import model.dao.UserDBManager;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DeleteUserServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session.");
            session.setAttribute("userListError", "Database manager is not available.");
            response.sendRedirect("UserListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            userDBManager.deleteUser(userId);
            logger.log(Level.INFO, "Deleted user with userId: {0}", userId);
            session.setAttribute("editUserSuccess", "User has been successfully deleted.");
            response.sendRedirect("UserListServlet");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid userId format.", e);
            session.setAttribute("userListError", "Invalid user ID.");
            response.sendRedirect("UserListServlet");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL exception while deleting user.", e);
            session.setAttribute("userListError", "Error deleting user.");
            response.sendRedirect("UserListServlet");
        }
    }
} 