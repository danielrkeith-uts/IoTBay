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
        ProductListEntryDBManager productListEntryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
        ProductDBManager productDBManager = (ProductDBManager) session.getAttribute("productDBManager");

        User user = (User) session.getAttribute("user");

        String productName = request.getParameter("productName");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        Product product = new Product(productName, "", price, 0);

        try {
            //int productId = productDBManager.addProduct(product);
            if (user != null && user instanceof Customer) {
                //customer is logged in
                Cart cart = ((Customer) user).getCart();
                //productListEntryDBManager.addProduct(cart.getCartId(), productId, quantity);

            } else {
                //customer is not logged in
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }
                cart.addProduct(product);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error while adding product to cart", e);
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
        ProductListEntryDBManager productListEntryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");

        try {
            if (user != null && user instanceof Customer) {
                // Logged-in user: load cart from DB
                Cart cart = ((Customer) user).getCart();
                List<ProductListEntry> productList = cart.getProductList();
                
                // for (ProductListEntry item : productList) {
                //     cart.addProduct(item);
                //     //cart.addProduct(productListEntryDBManager.getProductList(cart.getCartId()));
                // }
                // request.setAttribute("cart", cart);

            } else {
                // Guest user: load cart from session
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }
                request.setAttribute("cart", cart);
            }

            request.getRequestDispatcher("cart.jsp").forward(request, response);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving cart contents", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve cart.");
        }
    }
}

