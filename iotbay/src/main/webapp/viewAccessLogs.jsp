<%@ page import="java.util.List, model.ApplicationAccessLog, model.User, model.User.Role" %>
<%
    List<ApplicationAccessLog> logs = (List<ApplicationAccessLog>) request.getAttribute("accessLogs");
    model.User user = (model.User) session.getAttribute("user");

    String userRole = (user != null) ? user.getRole().name() : "No user in session";
    out.println("User Role: " + userRole); 
%>

<html>
<head>
    <title>Access Logs</title>
</head>
<body>
    <h1>Application Access Logs</h1>

    <table>
        <thead>
            <tr>
                <th>Date and Time</th>
                <th>Action</th>
                <% if (user != null && (user.getRole().name().equals("ADMIN") || user.getRole().name().equals("STAFF"))) { %>
                    <th>Actions</th>
                <% } %>
            </tr>
        </thead>
        <tbody>
            <% for (ApplicationAccessLog log : logs) { %>
                <tr>
                    <td><%= log.getDateTime() %></td>
                    <td><%= log.getApplicationAction().name() %></td>
                    <% if (user != null && (user.getRole().name().equals("ADMIN") || user.getRole().name().equals("STAFF"))) { %>
                        <td>
                          
                        </td>
                    <% } %>
                </tr>
            <% } %>
        </tbody>
    </table>

    <% if (user != null && (user.getRole().name().equals("ADMIN") || user.getRole().name().equals("STAFF"))) { %>
        <br>
        <a href="addaccesslogform.jsp">Add New Access Log</a>
    <% } %>
</body>
</html>
