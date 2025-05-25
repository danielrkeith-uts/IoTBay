<%@ page import="model.dao.UserDBManager" %>
<%@ page import="model.Customer" %>
<%
    String userIdStr = request.getParameter("userId");
    if (userIdStr == null) {
        response.sendRedirect("CustomerListServlet");
        return;
    }

    int userId = Integer.parseInt(userIdStr);

    UserDBManager dbManager = (UserDBManager) session.getAttribute("userDBManager");
    if (dbManager == null) {
        response.sendRedirect("error.jsp");
        return;
    }

    Customer customer = null;
    try {
        customer = dbManager.getCustomer(userId);
    } catch (Exception e) {
        response.sendRedirect("error.jsp");
        return;
    }

    if (customer == null) {
        response.sendRedirect("CustomerListServlet");
        return;
    }
%>


<html>
<head>
    <title>Confirm Delete Customer</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/customerlist.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</head>
<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>
    <div class="content">
        <h2>Delete Customer</h2>
        <p>Are you sure you want to delete the following customer?</p>
        <ul>
            <li><strong>Name:</strong> <%= customer.getFirstName() + " " + customer.getLastName() %></li>
            <li><strong>Email:</strong> <%= customer.getEmail() %></li>
            <li><strong>Status:</strong> <%= customer.isDeactivated() ? "Deactivated" : "Active" %></li>
        </ul>
        <form action="DeleteCustomerServlet" method="post">
            <input type="hidden" name="userId" value="<%= userId %>" />
            <input type="submit" class="btn btn-danger" value="Delete Customer" />
            <button class="btn btn-secondary" onclick="window.history.back(); return false;">Cancel</button>
        </form>
    </div>
</body>
</html>
