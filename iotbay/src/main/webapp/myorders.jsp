<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Enums.*"%>
<%@ page import="model.*"%>
<%@ page import="model.dao.OrderDBManager"%>

<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        // Retrieve session attributes
        User user = (User) session.getAttribute("user");
         if (!(session.getAttribute("user") instanceof Customer)) {
            response.sendRedirect("index.jsp");
            return;
        }

        if (session.getAttribute("orderDBManager") == null) {
            response.sendRedirect("index.jsp");
            return;
        }   

        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");

        String query = request.getParameter("query");
        List<Order> orders = orderManager.getOrder(query);

        if (query != null && !query.trim().isEmpty()) {
            String finalQuery = query.toLowerCase(); 
            orders = orders.stream()
                .filter(o -> o.getOrderId.contains(finalQuery))
                .collect(java.util.stream.Collectors.toList());
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
                    <a href="login.jsp">Login</a>
                <% } else { %>
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                            <li><a class="dropdown-item" href="myorders.jsp">My Orders</a></li>
                            <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                        </ul>
                    </div>
                <% } %>
                <a href="cart.jsp" class="bi bi-cart"></a>
            </navbar>
        </div>

        <form class="mb-4" method="get" action="myorders.jsp">
            <div class="input-group">
                <input type="text" name="query" class="form-control" placeholder="Search by OrderId (plain number)" value="<%= request.getParameter("query") != null ? request.getParameter("query") : "" %>" />
                <button type="submit" class="btn btn-outline-secondary">Search</button>
            </div>
        </form>
        <% if (order.isEmpty()) { %>
            <div class="alert alert-secondary" role="alert">
                No matching order found. Please try a different OrderId.
            </div>
        <% } else { %>
        <div class="row">
            <% for (Order o : orders) { %>
                <div class="col-md-4 mb-4">
                    <div class="card product-card p-3 h-100">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title"><%= o.getOrderId() %></h5>
                            <p class="card-text">
                                <strong>Products:</strong> 
                                <%= o.getProductList()%>
                            </p>
                            <p class="card-text">
                                <strong>Payment Status:</strong> 
                                <%= o.getPayment().getPaymentStatus() %>
                            </p>
                            <p class="card-text">
                                <strong>Date Placed:</strong> 
                                <%= o.getDatePlaced() %>
                            </p>
                            <p class="card-text">
                                <strong>Order Status:</strong> 
                                <%= o.getOrderStatus() %>
                            </p>
                            <form> 
                            <%-- action="CartServlet" method="post" --%>
                                <input type="hidden" name="orderId" value="<%= o.getOrderId() != null%>" />
                                <button type="submit" class="btn btn-primary mt-auto">
                                    Cancel Order
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            <% } %> 
        </div>
        <% } %>

    </body>
</html>