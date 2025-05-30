<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="model.User"%>
<%@ page import="model.ProductListEntry"%>
<%@ page import="model.Product"%>
<%@ page import="model.Cart"%>
<%@ page import="java.text.DecimalFormat"%>

<html>
   <jsp:include page="/ConnServlet" flush="true"/>
    <%
        String error = (String) session.getAttribute("cartError");
        session.removeAttribute("cartError"); 

        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

    %>
    <head>
        <link rel="stylesheet" href="css/main.css" />
        <link rel="stylesheet" href="css/cart.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body class="cart-page">
        <div class="banner">
            <h1>Place an Order</h1>
            <jsp:include page="navbar.jsp" />
            <%
                List<ProductListEntry> cartItems = cart.getProductList();
            %>
        </div>
        <h2>Your Shopping Cart</h2>
        <% if (cartItems == null || cartItems.isEmpty()) { %>
            <p>Your cart is empty.</p>
            <div class="cart-card">
                <a href="products.jsp" class="btn-green">Return to Products</a>
            </div>
        <% } else { %>
            <ul>
                <% 
                    for (ProductListEntry item : cartItems) {
                %>
                    <li><%= item.getProduct().getName() %> - $<%= item.getProduct().getCost() %> x <%= item.getQuantity() %></li>
                <%
                    }
                %>
            </ul>
            <% DecimalFormat df = new DecimalFormat("#.##"); %>
            <p><strong>Total: $<%= df.format(cart.totalCost()) %></strong></p>
            <div class="cart-card">
                <a href="products.jsp" class="btn-green">Return to Products</a>
                <form action="OrderServlet" method="post">
                    <button type="submit" class="btn-green">Buy Now</button>
                </form>
                <form action="SaveCartServlet" method="post" style="display:inline;">
                    <button type="submit" class="btn-green">Save Cart</button>
                </form>
            </div>
        <% } %>
    </body>
</html>