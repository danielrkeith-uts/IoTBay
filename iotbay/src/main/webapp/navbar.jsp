<%@ page import="model.User, model.Staff" %>
<%
    User user = (User) session.getAttribute("user");
    boolean isLoggedIn = user != null;
    boolean isAdmin = isLoggedIn && user.getRole() == User.Role.ADMIN;
    boolean isStaff = isLoggedIn && (user instanceof Staff);
%>

<navbar>
    <a href="index.jsp" class="active">Home</a>
    <a href="products.jsp">Products</a>

    <% if (!isLoggedIn) { %>
        <a href="login.jsp">Login</a>
    <% } else { %>
        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>

                <% if (isAdmin || isStaff) { %>
                    <li><a class="dropdown-item" href="customerlist.jsp">Customers</a></li>
                <% } %>

                <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
            </ul>
        </div>
    <% } %>
</navbar>
