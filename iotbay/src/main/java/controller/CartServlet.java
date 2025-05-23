package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import model.dao.*;

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
        
        CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
        if (cartDBManager == null) {
            throw new ServletException("CartDBManager retrieved from session is null");
        }

        ProductListEntryDBManager productListEntryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
        if (productListEntryDBManager == null) {
            throw new ServletException("ProductListEntryDBManager retrieved from session is null");
        }

        ProductDBManager productDBManager = (ProductDBManager) session.getAttribute("productDBManager");
        if (productDBManager == null) {
            throw new ServletException("ProductDBManager retrieved from session is null");
        }

        String productName = request.getParameter("productName");
        String productType = request.getParameter("productType");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        Product product;
        try {
            product = productDBManager.getProductByName(productName);
            Cart cart;
            if (user != null && user instanceof Customer) {
                cart = ((Customer) user).getCart();
                int cartId = cart.getCartId();
                cart.setCartId(cartId);
                ((Customer) user).setCart(cart);
                cart.addProduct(product, quantity);
                productListEntryDBManager.addProduct(cartId, product.getProductId(), quantity);
            } else {
                cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }
                cart.addProduct(product, quantity);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Database error while getting cart", e);
        }

        session.setAttribute("productName", productName);
        session.setAttribute("productType", productType);
        session.setAttribute("price", price);
        session.setAttribute("quantity", quantity);
        response.sendRedirect("cart.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
        if (cartDBManager == null) {
            throw new ServletException("CartDBManager retrieved from session is null");
        }
        
        try {
            Cart cart;
            if (user != null && user instanceof Customer) {
                cart = cartDBManager.getCart(user.getUserId());
            } else {
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


