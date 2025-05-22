<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, model.Customer, model.User" %>
<jsp:include page="/ConnServlet" flush="true"/>
<jsp:include page="/CustomerListServlet" flush="true"/>

<%
    User loggedInUser = (User) session.getAttribute("user");
    boolean isAdmin = loggedInUser != null && loggedInUser.getRole() == User.Role.ADMIN; 
    boolean isStaff = loggedInUser != null && loggedInUser.getRole() == User.Role.STAFF; 
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Customer</title>

    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/addcustomer.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</head>

<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>

    <h2 class="text-center mt-4">Add New Customer</h2>

    <form action="AddCustomerServlet" method="post" class="form-container">
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
        <div>
            <label>Type:</label>
            <select name="type" required>
                <option value="INDIVIDUAL">Individual</option>
                <option value="COMPANY">Company</option>
            </select>
        </div>

        <input type="submit" value="Add Customer" class="btn-green">

        <div class="text-center mt-4">
            <button class="btn btn-custom" type="button" onclick="goBack()">
                <i class="fas fa-arrow-left"></i> Back
            </button>
        </div>
    </form>

    <p style="color:red;"><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></p>
    <p style="color:green;"><%= request.getAttribute("success") != null ? request.getAttribute("success") : "" %></p>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>

</html>