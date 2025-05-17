<%@ page import="java.util.List, model.ApplicationAccessLog, model.User, model.User.Role" %>
<%
    // Fetch the logs from the request
    List<ApplicationAccessLog> logs = (List<ApplicationAccessLog>) request.getAttribute("accessLogs");
    model.User user = (model.User) session.getAttribute("user");

    // Debugging output for user role
    String userRole = (user != null) ? user.getRole().name() : "No user in session";
    out.println("User Role: " + userRole); // Debugging output to check role
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
                <% if (user != null && user.getRole().name().equals("ADMIN")) { %>
                    <th>Actions</th>
                <% } %>
            </tr>
        </thead>
        <tbody>
            <% for (ApplicationAccessLog log : logs) { %>
                <tr>
                    <td><%= log.getDateTime() %></td>
                    <td><%= log.getApplicationAction().name() %></td>
                    <%-- <% if (user != null && user.getRole().name().equals("ADMIN")) { %> --%>
                        <td>
                            <!-- Link to edit the access log -->
                            <a href="EditAccessLogServlet?logId=<%= log.getLogId() %>">Edit</a>
                        </td>
                    <% } %>
                </tr>
            <%-- <% } %> --%>
        </tbody>
    </table>
</body>
</html>
