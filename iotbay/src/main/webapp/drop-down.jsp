<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user != null) {
%>
    <div class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
            <li><a class="dropdown-item" href="customer_data.jsp">Customer Data</a></li>
            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
        </ul>
    </div>
<%
    }
%>
