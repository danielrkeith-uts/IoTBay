<%@ page import="model.User"%>
<html>
    <%
        User user = (User)session.getAttribute("user");
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="login.css" />
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
                    <a href="login.jsp" class="active">Login</a>
                <% } else { %>
                    <a href="logout.jsp">Logout</a>
                <% } %>
            </navbar>
        </div>
        <div class="content">
            <h2>Login</h2>
            <form action="welcome.jsp" methods="post">
                <label for="email">Email</label>
                <input name="email" placeholder="Enter your email" />
                <label for="password">Password</label>
                <input name="password" type="password" placeholder="Enter your password" />
                <input type="checkbox" name="tos" checked hidden>
                <input type="submit" />
            </form>
            <p>Don't have an account? Register <a href="register.jsp">here.</a></p>
        </div>
    </body>
</html>
