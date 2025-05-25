<%@ page import="model.User, model.ApplicationAccessLog, model.Enums.ApplicationAction" %>
<%@ page import="java.util.List, java.text.SimpleDateFormat" %>

<%
    User user = (User) request.getAttribute("user");
    List<ApplicationAccessLog> accessLogs = (List<ApplicationAccessLog>) request.getAttribute("accessLogs");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    if (user == null || accessLogs == null) {
        response.sendRedirect("UserListServlet");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Access Logs - IoTBay</title>
    
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
</head>
<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Access Logs for <%= user.getFirstName() + " " + user.getLastName() %></h1>
            <a href="UserListServlet" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Back to User List
            </a>
        </div>

        <div class="card mb-4">
            <div class="card-header">
                <h5 class="mb-0">User Details</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>User ID:</strong> <%= user.getUserId() %></p>
                        <p><strong>Name:</strong> <%= user.getFirstName() + " " + user.getLastName() %></p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Email:</strong> <%= user.getEmail() %></p>
                        <p><strong>Phone:</strong> <%= user.getPhone() %></p>
                    </div>
                </div>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Date/Time</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (accessLogs.isEmpty()) { %>
                        <tr>
                            <td colspan="2" class="text-center">No access logs found</td>
                        </tr>
                    <% } else { %>
                        <% for (ApplicationAccessLog log : accessLogs) { %>
                            <tr>
                                <td><%= dateFormat.format(log.getDateTime()) %></td>
                                <td>
                                    <% if (log.getApplicationAction() == ApplicationAction.LOGIN) { %>
                                        <span class="badge bg-success">Login</span>
                                    <% } else if (log.getApplicationAction() == ApplicationAction.LOGOUT) { %>
                                        <span class="badge bg-warning">Logout</span>
                                    <% } else { %>
                                        <%= log.getApplicationAction() %>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html> 