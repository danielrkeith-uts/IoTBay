<%@ page import="java.util.List, model.User, model.ApplicationAccessLog, model.Staff, model.dao.ApplicationAccessLogDBManager" %>
<%@ page session="true" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    ApplicationAccessLogDBManager logManager = (ApplicationAccessLogDBManager) session.getAttribute("applicationAccessLogDBManager");
    if (logManager == null) {
        throw new ServletException("ApplicationAccessLogDBManager not found in session");
    }
    boolean isAdmin = (user instanceof Staff) && ((Staff)user).isAdmin();
    List<ApplicationAccessLog> logs = logManager.getApplicationAccessLogs(user.getUserId());
%>
    <head>
        <link rel="stylesheet" href="css/main.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <jsp:include page="navbar.jsp" />
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