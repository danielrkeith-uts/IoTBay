package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import model.dao.ProductDBManager;
import model.dao.ProductListEntryDBManager;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(CartServlet.class.getName());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        Product product = new Product(productName, "", price, 0);

        try {
            Cart cart;
            if (user != null && user instanceof Customer) { //customer is logged in
                cart = ((Customer) user).getCart();
            } else { //customer is not logged in
                cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }
            }

            for (int i = 0; i < quantity; i++) {
                cart.addProduct(product);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database error while getting cart", e);
        }

        session.setAttribute("productName", productName);
        session.setAttribute("price", price);
        session.setAttribute("quantity", quantity);
        response.sendRedirect("cart.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        try {
            Cart cart;
            if (user != null && user instanceof Customer) {
                // Logged-in user: load cart from DB
                cart = ((Customer) user).getCart();
            } else {
                // Guest user: load cart from session
                cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }
            }
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving cart", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve cart.");
        }
    }
}

