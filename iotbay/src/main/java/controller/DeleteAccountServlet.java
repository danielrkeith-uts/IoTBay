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
import model.User;
import model.dao.ApplicationAccessLogDBManager;
import model.dao.UserDBManager;

@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(DeleteAccountServlet.class.getName());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        ApplicationAccessLogDBManager applicationAccessLogDBManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
        if (applicationAccessLogDBManager == null) {
            throw new ServletException("ApplicationAccessLogDBManager retrieved from session is null");
        }

        User user = (User) session.getAttribute("user");

        try {
            applicationAccessLogDBManager.anonymiseApplicationAccessLogs(user.getUserId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not anonymise application access logs");
            return;
        }

        try {
            userDBManager.deleteUser(user.getUserId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not delete user");
            return;
        }

        session.removeAttribute("user");
        request.getRequestDispatcher("index.jsp").include(request, response);
    }
}
