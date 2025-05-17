<%@ page import="java.util.List, model.ApplicationAccessLog" %>
<%
    List<ApplicationAccessLog> logs = (List<ApplicationAccessLog>) request.getAttribute("accessLogs");
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
                </tr>
            </thead>
            <tbody>
                <% for (ApplicationAccessLog log : logs) { %>
                    <tr>
                        <td><%= log.getDateTime() %></td>
                        <td><%= log.getApplicationAction().name() %></td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </body>
</html>
