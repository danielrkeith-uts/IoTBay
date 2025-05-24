package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.User;
import model.User.Role;
import model.dao.ApplicationAccessLogDBManager;
import model.Enums.ApplicationAction; 

@WebServlet("/UpdateAccessLogServlet")
public class UpdateAccessLogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int logId = Integer.parseInt(request.getParameter("logId"));
        String actionStr = request.getParameter("applicationAction");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || (user.getRole() != Role.ADMIN && user.getRole() != Role.STAFF)) {
            response.sendRedirect("unauthorized.jsp");  
            return;
        }

        ApplicationAccessLogDBManager dbManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        try {
            ApplicationAction newAction = ApplicationAction.valueOf(actionStr);  
            dbManager.updateApplicationAccessLog(logId, newAction);
            response.sendRedirect("ApplicationAccessLogServlet");
        } catch (SQLException e) {
            throw new ServletException("Failed to update access log", e);
        }
    }
}
