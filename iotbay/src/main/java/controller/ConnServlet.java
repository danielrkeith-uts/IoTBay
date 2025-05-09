package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.dao.ApplicationAccessLogDBManager;
import model.dao.DBConnector;
import model.dao.UserDBManager;

@WebServlet("/ConnServlet")
public class ConnServlet extends HttpServlet {
    private Logger logger;

    private DBConnector dbConnector;
    private Connection conn;
    private UserDBManager userDBManager;
    private ApplicationAccessLogDBManager applicationAccessLogDBManager;

    @Override
    public void init() {
        logger = Logger.getLogger(ConnServlet.class.getName());

        try {
            dbConnector = new DBConnector();
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Cannot connect to DB", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        // Instantiate all DBManagers
        try {
            conn = dbConnector.openConnection();

            if (session.getAttribute("userDBManager") == null) {
                userDBManager = new UserDBManager(conn);
                session.setAttribute("userDBManager", userDBManager);
            }

            if (session.getAttribute("applicationAccessLogDBManager") == null) {
                applicationAccessLogDBManager = new ApplicationAccessLogDBManager(conn);
                session.setAttribute("applicationAccessLogDBManager", applicationAccessLogDBManager);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not instantiate DBManagers", e);
        }
    }

    @Override
    public void destroy() {
        try {
            dbConnector.closeConnection();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not close DB connection", e);
        }
    }
}
