<%@ page import="model.Customer" %>
<%
    Customer customer = (Customer) request.getAttribute("customer");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<html>
<head>
    <link rel="stylesheet" href="main.css" />
</head>
<body>
    <div class="content">
        <h2>Edit Customer Details</h2>
        <% if (customer == null) { %>
            <p class="error">Customer not found or an error occurred.</p>
        <% } else { %>
            <form action="EditCustomerServlet" method="post">
                <input type="hidden" name="userId" value="<%= customer.getUserId() %>" />
                <div>
                    <label>Email:</label>
                    <input type="text" name="email" value="<%= customer.getEmail() %>" disabled />
                </div>
                <div>
                    <label>First Name:</label>
                    <input type="text" name="firstName" value="<%= customer.getFirstName() %>" />
                </div>
                <div>
                    <label>Last Name:</label>
                    <input type="text" name="lastName" value="<%= customer.getLastName() %>" />
                </div>
                <div>
                    <label>Phone:</label>
                    <input type="text" name="phone" value="<%= customer.getPhone() %>" />
                </div>
                <p class="error"><%= (error == null ? "" : error) %></p>
                <p class="success"><%= (success == null ? "" : success) %></p>
                <input type="submit" value="Update Customer">
            </form>
        <% } %>
    </div>
</body>
</html>
