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

@WebServlet("/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DeleteCustomerServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            logger.log(Level.SEVERE, "UserDBManager not found in session.");
            response.sendRedirect("errorPage.jsp");
            return;
        }

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            userDBManager.deleteUser(userId);
            logger.log(Level.INFO, "Deleted customer with userId: {0}", userId);
            response.sendRedirect("customerlist.jsp");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid userId format.", e);
            request.setAttribute("errorMessage", "Invalid customer ID.");
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL exception while deleting customer.", e);
            request.setAttribute("errorMessage", "Error deleting customer.");
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }
}
