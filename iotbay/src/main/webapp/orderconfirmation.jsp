<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <head>
        <title>Order Confirmation</title>
    </head>
    <body>
        <h1>Thank you for your order!</h1>
        <p>Your order ID is: <strong><%= request.getParameter("orderId") %></strong></p>
        <p>Save this ID in case you want to search for it later! To search for an order, navigate to the "My Account" tab, and click "My Orders".</p>
    </body>
</html>