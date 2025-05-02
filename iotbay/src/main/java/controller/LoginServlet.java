package controller;

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
import model.dao.UserDBManager;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(LoginServlet.class.getName());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        // TODO - stub
        User johnSmith = null;
        try {
            johnSmith = userDBManager.getUser(0);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not get user from DB");
        }

        session.setAttribute("user", johnSmith);
    }
}
