package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.dao.ApplicationAccessLogDBManager;
import model.dao.DBConnector;
import model.dao.UserDBManager;

@WebServlet("/ConnServlet")
public class ConnServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ConnServlet.class.getName());

    private DBConnector dbConnector;
    private Connection conn;
    private UserDBManager userDBManager;
    private ApplicationAccessLogDBManager applicationAccessLogDBManager;

    @Override
    public void init() {
        try {
            dbConnector = new DBConnector();
            logger.info("DBConnector initialized.");
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Cannot connect to DB", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            conn = dbConnector.openConnection();
            logger.info("Database connection opened.");

            if (session.getAttribute("userDBManager") == null) {
                userDBManager = new UserDBManager(conn);
                session.setAttribute("userDBManager", userDBManager);
                logger.info("UserDBManager added to session.");
            }

            if (session.getAttribute("applicationAccessLogDBManager") == null) {
                applicationAccessLogDBManager = new ApplicationAccessLogDBManager(conn);
                session.setAttribute("applicationAccessLogDBManager", applicationAccessLogDBManager);
                logger.info("ApplicationAccessLogDBManager added to session.");
            }

            logger.info("Session ID: " + session.getId());
            logger.info("UserDBManager in session: " + (session.getAttribute("userDBManager") == null ? "Not Found" : "Found"));

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error opening DB connection", e);
        }
    }

    @Override
    public void destroy() {
        try {
            dbConnector.closeConnection();
            logger.info("Database connection closed.");
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Could not close DB connection", e);
        }
    }
}
