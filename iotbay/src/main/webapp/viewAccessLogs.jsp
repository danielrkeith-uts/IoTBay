<%@ page import="java.util.List, model.ApplicationAccessLog, model.User, model.User.Role" flush="true" %>
<%
    List<ApplicationAccessLog> logs = (List<ApplicationAccessLog>) request.getAttribute("accessLogs");
    model.User user = (model.User) session.getAttribute("user");

    String userRole = (user != null) ? user.getRole().name() : "No user in session";
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Logs</title>

    <link rel="stylesheet" href="css/viewaccesslogs.css" />
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
     <script>
        function goBack() {
            window.history.back();
        }
    </script>
</head>

<body>

    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>

    <div class="container d-flex justify-content-center">
    <div class="log-table-container">
        <h2 class="text-center" >Application Access Logs</h2>

        <% if (user != null && (user.getRole().name().equals("ADMIN") || user.getRole().name().equals("STAFF"))) { %>
            <div class="text-center mb-4">
                <a href="addaccesslogform.jsp" class="btn btn-success">
                    <i class="fas fa-plus"></i> Add New Access Log
                </a>
            </div>
        <% } %>

        <div class="table-wrapper">
            <table class="table table-striped table-bordered">
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
           <div class="text-center mb-4">
    <button class="btn btn-custom" onclick="goBack()">
        <i class="fas fa-arrow-left"></i> Back
    </button>
</div>
        </div>
    </div>
</div>


</body>

</html>
