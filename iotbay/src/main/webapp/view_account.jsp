<%@ page import="model.Customer" %>
<%
    Customer customer = (Customer) request.getAttribute("customer");
%>
<html>
<head>
    <title>My Account</title>
</head>
<body>
    <h1>Account Details</h1>
    <p>First Name: <%= customer.getFirstName() %></p>
    <p>Last Name: <%= customer.getLastName() %></p>
    <p>Email: <%= customer.getEmail() %></p>
</body>
</html>
