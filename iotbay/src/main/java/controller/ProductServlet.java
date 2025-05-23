package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import model.dao.ProductDBManager;
import model.Enums.ProductType;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ProductDBManager productDBManager = (ProductDBManager) request.getSession().getAttribute("productDBManager");

            if (productDBManager == null) {
                // Not connected yet, maybe redirect to connection servlet or show error
                response.sendRedirect("ConnServlet");
                return;
            }

            String action = request.getParameter("action");

            if ("add".equals(action)) {
                ProductType type = ProductType.valueOf(request.getParameter("type").toUpperCase());
                Product p = new Product(
                    request.getParameter("name"),
                    request.getParameter("description"),
                    type,
                    Double.parseDouble(request.getParameter("cost")),
                    Integer.parseInt(request.getParameter("stock")),
                    request.getParameter("imageUrl") 
                );
                productDBManager.addProduct(p);
            } else if ("update".equals(action)) {
                String originalName = request.getParameter("originalName");
                ProductType type = ProductType.valueOf(request.getParameter("type").toUpperCase());
                Product updated = new Product(
                    request.getParameter("name"),
                    request.getParameter("description"),
                    type,
                    Double.parseDouble(request.getParameter("cost")),
                    Integer.parseInt(request.getParameter("stock")),
                    request.getParameter("imageUrl") 
                );
                productDBManager.updateProduct(originalName, updated);
            } else if ("delete".equals(action)) {
                String name = request.getParameter("originalName");
                productDBManager.deleteProduct(name);
            }

            response.sendRedirect("adminInventory.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            e.printStackTrace(response.getWriter());

        }
    }
}
