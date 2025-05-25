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
import model.Customer;
import model.Enums.ApplicationAction;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.UserDBManager;

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

        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        ApplicationAccessLogDBManager dbManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");

        try {
            Customer customer = userDBManager.getCustomer(userId);
            if (customer == null) {
                request.setAttribute("error", "Customer not found");
                request.getRequestDispatcher("addaccesslogform.jsp").forward(request, response);
                return;
            }

            ApplicationAccessLog log = new ApplicationAccessLog(action, new Date());
            dbManager.addApplicationAccessLog(userId, log);

            response.sendRedirect("CustomerListServlet");
        } catch (SQLException e) {
            throw new ServletException("Failed to add access log", e);
        }
    }
}
