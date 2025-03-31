<%@ page import="model.User"%>
<html>
    <%
        User user = (User)session.getAttribute("user");
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="register.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp">Products</a>
                <% if (user == null) { %>
                    <a href="login.jsp">Login</a>
                <% } else { %>
                    <a href="logout.jsp">Logout</a>
                <% } %>
            </navbar>
        </div>
        <div class="content">
            <h2>Register Here</h2>
            <form action="welcome.jsp" methods="post">
                <label>First Name: </label>
                <input type="text" name="firstName">
                <label>Last Name: </label>
                <input type="text" name="lastName">
                <label>Email: </label>
                <input type="text" name="email">
                <label>Phone Number: </label>
                <input type="text" name="phone">
                <label>Password: </label>
                <input type="password" name="password">
                <label>Gender: </label>
                <input type="text" name="gender">
                <label>Agree to TOS: </label>
                <input type="checkbox" name="tos">
                <input type="submit" value="Submit">
            </form>
        </div>
    </body>
</html>
