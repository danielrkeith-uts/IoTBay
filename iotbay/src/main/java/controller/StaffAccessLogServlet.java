package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ApplicationAccessLog;
import model.dao.ApplicationAccessLogDBManager;

@WebServlet("/StaffAccessLogServlet")
public class StaffAccessLogServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        ApplicationAccessLogDBManager logMgr =
            (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        String userIdParam = req.getParameter("userId");

        // missing manager or param? go back to list
        if (logMgr == null || userIdParam == null) {
            resp.sendRedirect("StaffListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            List<ApplicationAccessLog> logs = logMgr.getApplicationAccessLogs(userId);
            req.setAttribute("logs", logs);
            req.setAttribute("staffId", userId);
        } catch (NumberFormatException | SQLException e) {
            // in case of parse or SQL error, show an inline error
            req.setAttribute("errorMessage", "Could not load access logs.");
        }

        // forward into JSP to render the table (or the error message)
        req.getRequestDispatcher("/stafflogs.jsp").forward(req, resp);
    }
}
