package controller;

import model.dao.UserDBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/deactivateCustomer")  
public class DeactivateCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdParam = request.getParameter("id");

        if (userIdParam != null) {
            try {
                int userId = Integer.parseInt(userIdParam);

                HttpSession session = request.getSession();
                UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

                if (userDBManager != null) {
                    userDBManager.setCustomerDeactivated(userId, true);

                    response.sendRedirect("customerlist.jsp");
                } else {
                    request.setAttribute("error", "Database manager is not available.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid user ID format.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Failed to deactivate customer.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "No user ID provided.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
