<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="model.User"%>
<%@ page import="model.ProductListEntry"%>
<%@ page import="model.Product"%>
<%@ page import="model.Cart"%>

<html>
    <%
        User user = (User)session.getAttribute("user");
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="index.css" />
        <link rel="stylesheet" href="order.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Place and Order</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp" class="active">Products</a>
                <% if (user == null) { %>
                    <a href="login.jsp">Login</a>
                <% } else { %>
                    <a href="logout.jsp">Logout</a>
                <% } %>
                <a href="order.jsp">
                    <i class="bi bi-cart"></i>
                </a>
            </navbar>
            <%
                //from the products.jsp Add to Cart buttons
                String productName = request.getParameter("productName");
                String price = request.getParameter("price");
                String amount = request.getParameter("quantity");
                
                double cost = Double.parseDouble(price);
                int quantity = Integer.parseInt(amount);

                Cart cart = (Cart) session.getAttribute("cart");

                    if (cart == null) {
                        cart = new Cart();
                        session.setAttribute("cart", cart);
                    }

                List<ProductListEntry> cartItems = cart.getProductList();
                boolean productExists = false;

                for (ProductListEntry entry : cartItems) {
                    if (entry.getProduct().getName().equals(productName)) {
                        entry.setQuantity(entry.getQuantity() + quantity);
                        productExists = true;
                        break;
                    }
                }

                if (!productExists) {
                    Product product = new Product(productName, "", cost, 0);
                    cart.addProduct(product);
                }

            %>
        </div>
        <h2>Your Shopping Cart</h2>
        <% if (cartItems == null || cartItems.isEmpty()) { %>
            <p>Your cart is empty.</p>
        <% } else { %>
            <ul>
                <% 
                    for (ProductListEntry item : cartItems) {
                %>
                    <li><%= item.getProduct().getName() %> - $<%= item.getProduct().getCost() %> x <%= item.getQuantity() %></li>
                <%
                    }
                %>
                <p><strong>Total: $<%= cart.totalCost() %></strong></p>
            </ul>
        <% } %>
    </body>
</html>