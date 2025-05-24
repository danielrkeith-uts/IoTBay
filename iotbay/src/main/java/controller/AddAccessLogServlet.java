package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ApplicationAccessLog;
import model.User;
import model.Enums.ApplicationAction;
import model.dao.ApplicationAccessLogDBManager;

@WebServlet("/AddAccessLogServlet")
public class AddAccessLogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF)) {
            response.sendRedirect("unauthorized.jsp");
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        String actionStr = request.getParameter("applicationAction");
        ApplicationAction action = ApplicationAction.valueOf(actionStr);

        ApplicationAccessLogDBManager dbManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        try {
            ApplicationAccessLog log = new ApplicationAccessLog(action, new Date());
            dbManager.addApplicationAccessLog(userId, log);

            response.sendRedirect("ApplicationAccessLogServlet");
        } catch (SQLException e) {
            throw new ServletException("Failed to add access log", e);
        }
    }
}
