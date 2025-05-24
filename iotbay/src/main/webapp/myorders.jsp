<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Enums.*"%>
<%@ page import="model.*"%>
<%@ page import="model.dao.*"%>

<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        User user = (User) session.getAttribute("user");

        Order order = null;
        String orderError = null;

        if (user == null || !(user instanceof Customer) || orderDBManager == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<Order> orders = orderDBManager.getAllCustomerOrders(user.getUserId());

        String query = request.getParameter("query");
        if (query != null && !query.trim().isEmpty()) {
            try {
                int searchId = Integer.parseInt(query.trim());
                orders = orders.stream()
                    .filter(o -> o.getOrderId() == searchId)
                    .collect(java.util.stream.Collectors.toList());
            } catch (NumberFormatException e) {
                request.setAttribute("orderError", "Invalid order ID.");
            }
        }
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="cart.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body class="cart-page">
        <div class="banner">
            <h1>My Orders</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp">Products</a>
                <% if (user == null) { %>
                    <a href="LoginPageServlet">Login</a>
                <% } else { %>
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                            <li><a class="dropdown-item" href="myorders.jsp">My Orders</a></li>
                            <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                            <li><a class="dropdown-item" href="LogoutServlet">Logout</a></li>
                            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                        </ul>
                    </div>
                <% } %>
                <a href="cart.jsp" class="bi bi-cart"></a>
            </navbar>
        </div>
        <form method="get" action="myorders.jsp" class="mb-4">
            <div class="input-group">
                <input type="text" name="query" class="form-control" placeholder="Search by Order ID" value="<%= query != null ? query : "" %>" />
                <button type="submit" class="btn btn-outline-secondary">Search</button>
            </div>
        </form>
        <% if (orders.isEmpty()) { %>
            <div class="alert alert-secondary" role="alert">
                No matching order found. Please try a different OrderId.
            </div>
        <% } else { %>
            <% if (orders.isEmpty()) { %>
                <div class="alert alert-warning">No orders found.</div>
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
                                <%-- <form action="CartServlet" method="post"> --%>
                                    <%-- <input type="hidden" name="orderId" value="<%= order.getOrderId() != 0%>" />
                                    <button type="submit" class="btn btn-primary mt-auto">
                                        Cancel Order
                                    </button>
                                </form> --%>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        <% } %> 
    </body>
</html>