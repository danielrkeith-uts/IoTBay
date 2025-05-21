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
     <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>

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
