package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.dao.*;

@WebServlet("/ConnServlet")
public class ConnServlet extends HttpServlet {
    private Logger logger;

    private DBConnector dbConnector;
    private Connection conn;
    private UserDBManager userDBManager;
    private ProductDBManager productDBManager;
    private ProductListEntryDBManager productListEntryDBManager;
    private CartDBManager cartDBManager;
    private OrderDBManager orderDBManager;
    private ShipmentDBManager shipmentDBManager;
    private DeliveryDBManager deliveryDBManager;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("connection") == null) {
            try {
                // Adjust your JDBC setup here
                Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/kiter/Desktop/Uni/IoTBay/IoTBay/database/database.db");
                session.setAttribute("connection", conn);

                // Create DBManagers using this connection
                session.setAttribute("userDBManager", new UserDBManager(conn));
                session.setAttribute("applicationAccessLogDBManager", new ApplicationAccessLogDBManager(conn));
                session.setAttribute("productListEntryDBManager", new ProductListEntryDBManager(conn));
                session.setAttribute("deliveryDBManager", new DeliveryDBManager(conn));
                session.setAttribute("orderDBManager", new OrderDBManager(conn));
                session.setAttribute("shipmentDBManager", new ShipmentDBManager(conn));
                session.setAttribute("productDBManager", new ProductDBManager(conn));
                session.setAttribute("cartDBManager", new CartDBManager(conn));

            } catch (ClassNotFoundException | SQLException e) {
                throw new ServletException("Database connection setup failed", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
