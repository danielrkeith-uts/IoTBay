package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDBManager;

@WebServlet("/DeactivateStaffServlet")
public class DeactivateStaffServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");
        String userIdParam = req.getParameter("userId");

        // if we don't have a manager or no ID, go back to list
        if (mgr == null || userIdParam == null) {
            resp.sendRedirect("StaffListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            mgr.deactivateStaff(userId);
        } catch (NumberFormatException | SQLException e) {
            // you might log this or set a flash message in session
            // e.g. session.setAttribute("errorMessage", "Could not deactivate staff.");
        }

        // always redirect back so the list refreshes
        resp.sendRedirect("StaffListServlet");
    }
}

