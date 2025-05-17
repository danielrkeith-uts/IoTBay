<%@ page import="model.User, model.Enums.ApplicationAction" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF)) {
        response.sendRedirect("unauthorized.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Access Log</title>
    <link rel="stylesheet" href="css/main.css" />
     <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet" href="css/addaccesslogform.css" />
<script>
        function goBack() {
            window.history.back();
        }
    </script>
</head>

<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <navbar>
            <a href="index.jsp">Home</a>
            <a href="products.jsp">Products</a>
            <div class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                    <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                    <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                    <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                </ul>
            </div>
        </navbar>
    </div>

    <div class="form-container">
        <h1>Add Access Log</h1>
        <form action="AddAccessLogServlet" method="post">
            <label for="applicationAction">Action:</label>
            <select name="applicationAction" required>
                <option value="LOGIN">LOGIN</option>
                <option value="LOGOUT">LOGOUT</option>
                <option value="REGISTER">REGISTER</option>
                <option value="ADD_TO_CART">ADD TO CART</option>
                <option value="PLACE_ORDER">PLACE ORDER</option>
            </select>

            <label for="userId">User ID:</label>
            <input type="text" name="userId" required/>

            <button type="submit">Add Log</button>
            <div class="text-center mt-4">
        <button class="btn btn-secondary" onclick="goBack()">
            <i class="fas fa-arrow-left"></i> Back
        </button>
    </div>
        </form>
    </div>
</body>
</html>
