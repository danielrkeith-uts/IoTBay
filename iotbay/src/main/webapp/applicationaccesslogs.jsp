<%@ page import="java.util.List, model.User, model.ApplicationAccessLog"%>
<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <jsp:include page="/RequiresUserServlet" flush="true"/>
    <jsp:include page="/ApplicationAccessLogServlet" flush="true"/>
    <%
        User user = (User)session.getAttribute("user");

        List<ApplicationAccessLog> logs = user.getApplicationAccessLogs();
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
                    </ul>
                </div>
            </navbar>
        </div>
        <div class="content">
            <h2>Application Access Logs</h2>
            <table class="table table-striped">
                <tr>
                    <th scope="col">Date - time</th>
                    <th scope="col">Action</th>
                </tr>
                <% for (ApplicationAccessLog log : logs) { %>
                    <tr>
                        <td><%= log.getDateTime() %></td>
                        <td><%= log.getApplicationAction().name() %>
                    </tr>
                <% } %>
            </table>
        </div>
    </body>
</html>