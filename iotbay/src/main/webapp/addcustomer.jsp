<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Customer</title>
</head>
<body>
    <h2>Add New Customer</h2>
    <form action="AddCustomerServlet" method="post">
        <div>
            <label>First Name:</label>
            <input type="text" name="firstName" required>
        </div>
        <div>
            <label>Last Name:</label>
            <input type="text" name="lastName" required>
        </div>
        <div>
            <label>Email:</label>
            <input type="email" name="email" required>
        </div>
        <div>
            <label>Phone:</label>
            <input type="text" name="phone">
        </div>
        <div>
            <label>Password:</label>
            <input type="password" name="password" required>
        </div>
        <input type="submit" value="Add Customer">
    </form>

    <p style="color:red;"><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></p>
    <p style="color:green;"><%= request.getAttribute("success") != null ? request.getAttribute("success") : "" %></p>
</body>
</html>
