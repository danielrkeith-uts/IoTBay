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

@WebServlet("/ReactivateStaffServlet")
public class ReactivateStaffServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");
        String userIdParam = req.getParameter("userId");

        if (mgr == null || userIdParam == null) {
            resp.sendRedirect("StaffListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            mgr.reactivateStaff(userId);
        } catch (NumberFormatException | SQLException e) {
            // optionally log or flash message:
            // session.setAttribute("errorMessage", "Could not reactivate staff.");
        }

        resp.sendRedirect("StaffListServlet");
    }
}


