package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import model.dao.*;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter("/*")
public class DatabaseFilter implements Filter {
    private static final Logger logger = Logger.getLogger(DatabaseFilter.class.getName());
    private static DataSource dataSource;

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl("jdbc:sqlite:database.db");
            ds.setEnforceForeignKeys(true);
            dataSource = ds;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing database connection pool", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        Connection conn = null;

        try {
            // Get a connection from the pool
            conn = dataSource.getConnection();
            conn.setAutoCommit(true);

            // Store the connection in request for this request cycle
            request.setAttribute("dbConnection", conn);

            // Initialize DB managers if they don't exist in session
            if (session.getAttribute("userDBManager") == null) {
                session.setAttribute("userDBManager", new UserDBManager(conn));
            }
            if (session.getAttribute("paymentDBManager") == null) {
                session.setAttribute("paymentDBManager", new PaymentDBManager(conn));
            }
            if (session.getAttribute("cartDBManager") == null) {
                session.setAttribute("cartDBManager", new CartDBManager(conn));
            }
            if (session.getAttribute("orderDBManager") == null) {
                session.setAttribute("orderDBManager", new OrderDBManager(conn));
            }
            if (session.getAttribute("productDBManager") == null) {
                session.setAttribute("productDBManager", new ProductDBManager(conn));
            }
            if (session.getAttribute("productListEntryDBManager") == null) {
                session.setAttribute("productListEntryDBManager", new ProductListEntryDBManager(conn));
            }
            if (session.getAttribute("shipmentDBManager") == null) {
                session.setAttribute("shipmentDBManager", new ShipmentDBManager(conn));
            }
            if (session.getAttribute("deliveryDBManager") == null) {
                session.setAttribute("deliveryDBManager", new DeliveryDBManager(conn));
            }
            if (session.getAttribute("applicationAccessLogDBManager") == null) {
                session.setAttribute("applicationAccessLogDBManager", new ApplicationAccessLogDBManager(conn));
            }

            // Continue with the request
            chain.doFilter(request, response);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error in filter", e);
            throw new ServletException("Database error", e);
        } finally {
            // Always close the connection after the request is complete
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "Error closing database connection", e);
                }
            }
        }
    }

    @Override
    public void destroy() {
        // Nothing to clean up
    }
} 