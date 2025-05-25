<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Enums.*"%>
<%@ page import="model.*"%>
<%@ page import="model.dao.*"%>

<html>
    <%
        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        User user = (User) session.getAttribute("user");

        Order order = null;
        String orderError = null;

        if (user == null || !(user instanceof Customer) || orderDBManager == null) {
            response.sendRedirect("ConnServlet?redirectTo=myorders.jsp");
            return;
        }

        List<Order> orders = orderDBManager.getAllCustomerOrders(user.getUserId());

        String query = request.getParameter("query");
        try {
            query = query.trim();
            if (query.matches("\\d+")) {
                // Numeric: search by order ID
                int searchId = Integer.parseInt(query);
                orders = orders.stream()
                    .filter(o -> o.getOrderId() == searchId)
                    .collect(java.util.stream.Collectors.toList());
            } else if (query.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // Date format: search by datePlaced (YYYY-MM-DD)
                String finalQuery = query;
                orders = orders.stream()
                    .filter(o -> o.getDatePlaced().toString().startsWith(finalQuery))
                    .collect(java.util.stream.Collectors.toList());
            } else {
                request.setAttribute("orderError", "Please enter a valid Order ID or date (YYYY-MM-DD).");
            }
        } catch (Exception e) {
            request.setAttribute("orderError", "Search error.");
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
            <h1>My Orders</h1>
            <jsp:include page="navbar.jsp" />
        </div>
        <form method="get" action="myorders.jsp" class="mb-4">
            <div class="input-group">
                <input type="text" name="query" class="form-control" placeholder="Search by Order ID or Date (YYYY-MM-DD)" value="<%= query != null ? query : "" %>" />
                <button type="submit" class="btn btn-outline-secondary">Search</button>
            </div>
        </form>
        <% if (orders.isEmpty()) { %>
            <div class="alert alert-secondary" role="alert">
                No matching order found. Please try a different OrderId.
            </div>
        <% } else { %>
            <div class="order-list">
                <% for (Order o : orders) { %>
                    <div class="card mb-3">
                        <div class="card-body">
                            <h5 class="card-title">Order #<%= o.getOrderId() %></h5>
                            <p><strong>Date:</strong> <%= o.getDatePlaced() %></p>
                            <p><strong>Status:</strong> <%= o.getOrderStatus() %></p>
                            <p><strong>Products:</strong></p>
                            <ul>
                                <% for (ProductListEntry entry : o.getProductList()) { %>
                                    <li><%= entry.getProduct().getName() %> × <%= entry.getQuantity() %></li>
                                <% } %>
                            </ul>
                            <% if (o.getOrderStatus() == OrderStatus.SAVED) { %>
                                <form action="UpdateOrderServlet" method="get">
                                <input type="hidden" name="orderId" value="<%= o.getOrderId() %>" />
                                    <button type="submit" class="btn-green">
                                        Update Order
                                    </button>
                                </form>
                                <form action="CancelOrderServlet" method="post">
                                <input type="hidden" name="orderId" value="<%= o.getOrderId() %>" />
                                    <button type="submit" class="btn-green">
                                        Cancel Order
                                    </button>
                                </form>
                            <% } %>
                        </div>
                    </div>
                </div>
            <% } %>
        <% } %> 
    </body>
</html>