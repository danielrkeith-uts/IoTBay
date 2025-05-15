<%@ page import="model.User, model.Staff"%>
<html>
    <%
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String error = (String) session.getAttribute("accountError");
        session.removeAttribute("accountError");

        String success = (String) session.getAttribute("accountSuccess");
        session.removeAttribute("accountSuccess");

        User user = (User)session.getAttribute("user");

        boolean isStaff = user instanceof Staff;
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
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
        <div class="content">
            <h2>Account Details</h2>
            <form action="AccountDetailsServlet" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="text" name="email" class="form-control" disabled value="<%= user.getEmail() %>" />
                </div>
                <div class="mb-3">
                    <label for="firstName" class="form-label">First Name (optional)</label>
                    <input type="text" name="firstName" class="form-control" value="<%= user.getFirstName() %>" />
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Last Name (optional)</label>
                    <input type="text" name="lastName" class="form-control" value="<%= user.getLastName() %>" />
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone Number (optional)</label>
                    <input type="text" name="phone" class="form-control" value="<%= user.getPhone() %>" />
                </div>
                <% if (isStaff) { %>
                    <div class="mb-3">
                        <label for="staffCardId" class="form-label">Staff Card ID</label>
                        <input type="text" name="staffCardId" class="form-control" value="<%= ((Staff) user).getStaffCardId() %>" />
                    </div>
                <% } %>
                <p>Password: <a href="changepassword.jsp">Change password</a></p>
                <p class="error"><%= (error == null ? "" : error) %></p>
                <p class="success"><%= (success == null ? "" : success) %></p>
                <input type="submit" class="btn-green" value="Save changes">
            </form>
        </div>
    </body>
</html>
