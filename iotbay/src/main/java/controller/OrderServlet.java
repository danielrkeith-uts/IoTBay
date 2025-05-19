package controller;

import model.*;
import model.dao.CartDBManager;
import model.dao.DBConnector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    private DBConnector dbConnector;
    private Connection conn;
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(OrderServlet.class.getName());

        try {
            dbConnector = new DBConnector();
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Cannot connect to DB", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        session.setAttribute("firstName", firstName);
        session.setAttribute("lastName", lastName);
        session.setAttribute("email", email);
        session.setAttribute("phone", phone);

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getProductList().isEmpty()) {
            session.setAttribute("cartError", "Your cart is empty.");
            response.sendRedirect("cart.jsp");
            return;
        }

        try {
            Connection conn = (Connection) getServletContext().getAttribute("dbconn");
            conn = dbConnector.openConnection();
            if (conn == null) {
                throw new SQLException("No database connection.");
            }

            CartDBManager dbManager = new CartDBManager(conn);
            
            Date now = new Date();
            cart.setLastUpdated(now);
            
            int cartId = dbManager.addCart(new java.sql.Date(now.getTime()));
            cart.setCartId(cartId);

            session.setAttribute("cart", cart);
            response.sendRedirect("order.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding cart.");
        } finally {
            if (conn != null) {
                try {
                    dbConnector.closeConnection();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "Failed to close DB connection", e);
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("order.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing order.");
        }
    }
}

