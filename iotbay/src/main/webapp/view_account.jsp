<%@ page import="model.User, model.Staff" %>
<%
    // Retrieve the user object from the session
    User user = (User) session.getAttribute("user");

    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Check if the user is an instance of Staff (admin is a type of staff)
    boolean isAdmin = user instanceof Staff && ((Staff) user).isAdmin();
%>

<html>
<head>
    <title>My Account</title>
</head>
<body>
    <h1>Account Details</h1>

    <p><strong>First Name:</strong> <%= user.getFirstName() %></p>
    <p><strong>Last Name:</strong> <%= user.getLastName() %></p>
    <p><strong>Email:</strong> <%= user.getEmail() %></p>
    <p><strong>Phone:</strong> <%= user.getPhone() %></p>

    <p><strong>Password:</strong> ******** <small>(hidden for security)</small></p>

    <br>
    <a href="EditAccountServlet">Edit Account</a>
    <a href="OrderHistoryServlet">View Order History</a>

    <%-- Show admin-specific options if user is an admin --%>
    <% if (isAdmin) { %>
        <br>
        <a href="ManageUsersServlet">Manage Users</a>
        <a href="ViewAllOrdersServlet">View All Orders</a>
        <a href="AdminDashboardServlet">Admin Dashboard</a>
    <% } %>
</body>
</html>
