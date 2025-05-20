<%@ page import="model.User, model.Staff" %>
<%
    User user = (User) session.getAttribute("user");
    boolean isLoggedIn = user != null;
    boolean isAdmin = isLoggedIn && user.getRole() == User.Role.ADMIN;
    boolean isStaff = isLoggedIn && (user instanceof Staff);

    String currentPage = request.getRequestURI();
    currentPage = currentPage.substring(currentPage.lastIndexOf("/") + 1);
%>

<navbar>
    <a href="index.jsp" class="<%= currentPage.equals("index.jsp") ? "active" : "" %>">Home</a>
    <a href="products.jsp" class="<%= currentPage.equals("products.jsp") ? "active" : "" %>">Products</a>

    <% if ((isAdmin || isStaff)) { %>
        <a href="customerlist.jsp" class="<%= currentPage.equals("customerlist.jsp") ? "active" : "" %>">Customers</a>
    <% } %>

    <% if (!isLoggedIn) { %>
        <a href="login.jsp" class="<%= currentPage.equals("login.jsp") ? "active" : "" %>">Login</a>
    <% } else { %>
        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle <%= currentPage.equals("account.jsp") 
                || currentPage.equals("applicationaccesslogs.jsp") 
                || currentPage.equals("deleteaccount.jsp") 
                ? "active" : "" %>" 
               data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">
                My Account
            </a>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item <%= currentPage.equals("account.jsp") ? "active" : "" %>" href="account.jsp">Account Details</a></li>
                <li><a class="dropdown-item <%= currentPage.equals("applicationaccesslogs.jsp") ? "active" : "" %>" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                <li><a class="dropdown-item <%= currentPage.equals("logout.jsp") ? "active" : "" %>" href="logout.jsp">Logout</a></li>
                <li><a class="dropdown-item text-danger <%= currentPage.equals("deleteaccount.jsp") ? "active" : "" %>" href="deleteaccount.jsp">Delete Account</a></li>
            </ul>
        </div>
    <% } %>
</navbar>
