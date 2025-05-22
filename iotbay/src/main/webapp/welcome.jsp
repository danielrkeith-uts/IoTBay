<%@ page import="model.User, model.Customer"%>
<html>
    <%!
        public boolean isNullOrEmpty(String str) {
            return str == null || str.isEmpty();
        }
    %>
    <%
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        boolean isStaff = false;
    if (user != null && user instanceof model.Staff) {
        isStaff = true;
    }
    String firstName = (user != null) ? user.getFirstName() : "";
        //String firstName = user.getFirstName();
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
                <% if (isStaff) { %>
                            <a href="adminInventory.jsp">Manage Inventory</a>
                        <% } %>
                <% if (user == null) { %>
                    <a href="login.jsp">Login</a>
                <% } else { %>
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                            <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                        
                            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                        </ul>
                    </div>
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
                <p>Click <a href="account.jsp">here</a> to view your account details.</p>
            </div>
        </div>
    </body>
</html>
