<%@ page import="model.User, model.Customer"%>
<html>
    <%!
        public boolean isNullOrEmpty(String str) {
            return str == null || str.isEmpty();
        }
    %>
    <%
        User user = (User) session.getAttribute("user");
        String firstName = user.getFirstName();
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="raisedbox.css" />
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
        <div class="container">
            <div class="raisedbox">
                <div class="check-icon text-center">
                    <i class="fas fa-check-circle"></i>
                </div>
                <h2 class="animated-title">Welcome<%= (isNullOrEmpty(firstName) ? "" : ", " + firstName)  %>!</h2>
                <p>Click <a href="index.jsp">here</a> to proceed to the main page.</p>
            </div>
        </div>
    </body>
</html>
