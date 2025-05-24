<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="model.*"%>

<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <% User user = (User) session.getAttribute("user"); %>
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
            <h1>Confirmation</h1>
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
                            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                        </ul>
                    </div>
                <% } %>
                <a href="cart.jsp" class="bi bi-cart active"></a>
            </navbar>
        </div>
        <h1>You've saved a cart!</h1>
        <p>Your order ID is: <strong><%= request.getParameter("orderId") %></strong></p>
        <p>To update your order at a later date, you can login, navigate to the "My Account" tab, then "My Orders". You can either search for the order or find it in your list. Then click the "Update" or "Delete" buttons as required.</p>
    </body>
</html>