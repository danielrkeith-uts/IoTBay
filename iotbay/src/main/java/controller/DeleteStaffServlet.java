package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.UserDBManager;

@WebServlet("/DeleteStaffServlet")
public class DeleteStaffServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");
        String id = req.getParameter("id");

        // if we’re mis‐wired, just bounce back
        if (mgr == null || id == null) {
            resp.sendRedirect("StaffListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(id);
            mgr.deleteUser(userId);
            // after delete, refresh the list
            resp.sendRedirect("StaffListServlet");
        } 
        catch (NumberFormatException | SQLException e) {
            // on error, show the error message at top of list
            req.setAttribute("errorMessage", "Could not delete staff member.");
            req.getRequestDispatcher("StaffListServlet")
               .forward(req, resp);
        }
    }
}

