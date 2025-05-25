<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="model.*"%>

<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <% User user = (User) session.getAttribute("user"); %>
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
            <h1>Confirmation</h1>
            <jsp:include page="navbar.jsp" />
        </div>
        <h1>Thank you for your order!</h1>
        <p>Your order ID is: <strong><%= request.getParameter("orderId") %></strong></p>
        <p>Save this ID in case you want to search for it later! To search for an order, login first, then navigate to the "My Account" tab, and click "My Orders".</p>
        <p>Note: If you didn't link this order to an account, you won't be able to search for it even after registering.</p>
    </body>
</html>