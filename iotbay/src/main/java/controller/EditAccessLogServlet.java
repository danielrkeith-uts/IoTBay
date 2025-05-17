package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ApplicationAccessLog;
import model.dao.ApplicationAccessLogDBManager;

@WebServlet("/EditAccessLogServlet")
public class EditAccessLogServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int logId = Integer.parseInt(request.getParameter("logId"));

        HttpSession session = request.getSession();
        ApplicationAccessLogDBManager dbManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        try {
            ApplicationAccessLog log = dbManager.getApplicationAccessLogById(logId);
            
            if (log != null) {
                request.setAttribute("log", log);
                request.getRequestDispatcher("/editAccessLog.jsp").forward(request, response);
            } else {
                response.sendRedirect("error.jsp");
            }
        } catch (SQLException e) {
            throw new ServletException("Failed to retrieve access log for editing", e);
        }
    }
}
