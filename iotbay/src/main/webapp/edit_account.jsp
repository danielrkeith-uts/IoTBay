<%@ page import="model.Customer" %>
<%
    Customer customer = (Customer) request.getAttribute("customer");
    String error = (String) request.getAttribute("error");
%>
<html>
<head>
    <title>Edit Account</title>
</head>
<body>
    <h1>Edit Account Details</h1>

    <% if (error != null) { %>
        <p style="color:red;"><%= error %></p>
    <% } %>

    <form action="EditAccountServlet" method="post">
        <label>First Name:</label><br>
        <input type="text" name="firstName" value="<%= customer.getFirstName() %>" required><br><br>

        <label>Last Name:</label><br>
        <input type="text" name="lastName" value="<%= customer.getLastName() %>" required><br><br>

        <label>Email:</label><br>
        <input type="email" name="email" value="<%= customer.getEmail() %>" required><br><br>

        <label>Phone:</label><br>
        <input type="text" name="phone" value="<%= customer.getPhone() %>" required><br><br>

        <label>Password:</label><br>
        <input type="password" value="********" disabled>
        <small><a href="ChangePassword.jsp">Change Password</a></small><br><br>

        <input type="submit" value="Save Changes">
    </form>

    <br>
    <a href="ViewAccountServlet">Cancel</a>
</body>
</html>
