<%@ page import="model.User, model.ExtendedUser, model.Staff, model.Customer" %>
<%@ page import="java.util.List, java.util.ArrayList" %>

<%
    List<ExtendedUser> users = (List<ExtendedUser>) request.getAttribute("users");
    if (users == null) {
        users = new ArrayList<>();
    }

    String searchTerm = (String) request.getAttribute("searchTerm");
    if (searchTerm == null) {
        searchTerm = "";
    }

    String error = (String) session.getAttribute("userListError");
    String editSuccess = (String) session.getAttribute("editUserSuccess");
    
    // Clear messages after displaying
    session.removeAttribute("userListError");
    session.removeAttribute("editUserSuccess");

    // Check user permissions
    User loggedInUser = (User) session.getAttribute("user");
    boolean isAdmin = false;
    if (loggedInUser instanceof Staff) {
        isAdmin = ((Staff) loggedInUser).isAdmin();
    } else if (loggedInUser instanceof ExtendedUser) {
        isAdmin = ((ExtendedUser) loggedInUser).isAdmin();
    }
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>User Management - IoTBay</title>

    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/userlist.css" />
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
        rel="stylesheet" />
    <link
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
        rel="stylesheet" />

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</head>

<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>

    <div class="user-section">
        <h1>User Management</h1>
        
        <% if (error != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <% } %>
        
        <% if (editSuccess != null) { %>
            <div class="alert alert-success" role="alert">
                <%= editSuccess %>
            </div>
        <% } %>

        <div class="search-section">
            <% if (isAdmin) { %>
                <div>
                    <a href="CreateUserServlet" class="btn btn-success">
                        <i class="fas fa-user-plus"></i> Create User
                    </a>
                </div>
            <% } %>
            <div class="search-box">
                <form action="UserListServlet" method="GET" class="d-flex">
                    <input type="text" name="search" value="<%= searchTerm %>" 
                           class="form-control me-2" 
                           placeholder="Search by name, email, or phone number...">
                    <button type="submit" class="btn btn-warning">
                        <i class="fas fa-search"></i> Filter
                    </button>
                </form>
            </div>
        </div>

        <div class="user-grid">
            <% for (ExtendedUser user : users) { %>
                <div class="user-card <%= user.isDeactivated() ? "deactivated" : "" %>">
                    <div class="content-wrapper">
                        <p><strong>ID:</strong> <%= user.getUserId() %></p>
                        <p><strong>Name:</strong> <%= user.getFirstName() + " " + user.getLastName() %></p>
                        <p><strong>Email:</strong> <%= user.getEmail() %></p>
                        <p><strong>Phone:</strong> <%= user.getPhone() %></p>
                        <p>
                            <strong>Type:</strong><br>
                            <% if (user.isStaff()) { %>
                                <span >Staff</span>
                                <% if (user.isAdmin()) { %>
                                    <span>Admin</span>
                                <% } %>
                            <% } %>
                            <% if (user.isCustomer()) { %>
                                <span >
                                    Customer (<%= user.getCustomerType() %>)
                                </span>
                            <% } %>
                        </p>
                        <p>
                            <strong>Status:</strong>
                            <% if (user.isDeactivated()) { %>
                                <span style="color: #dc3545;">Deactivated</span>
                            <% } else { %>
                                <span style="color: #5e8c4e;">Active</span>
                            <% } %>
                        </p>
                    </div>
                    
                    <% if (isAdmin) { %>
                        <div class="actions">
                            <div class="button-row">
                                <a href="EditUserServlet?userId=<%= user.getUserId() %>" class="btn btn-primary">
                                    <i class="fas fa-edit"></i> Edit
                                </a>
                                <a href="ViewUserAccessLogsServlet?userId=<%= user.getUserId() %>" class="btn btn-primary">
                                    <i class="fas fa-history"></i> View Logs
                                </a>
                                <button type="button" class="btn btn-primary" onclick="document.getElementById('deactivateForm<%= user.getUserId() %>').submit();">
                                    <i class="fas fa-user-slash"></i> <%= user.isDeactivated() ? "Reactivate" : "Deactivate" %>
                                </button>
                                <form id="deactivateForm<%= user.getUserId() %>" action="DeactivateUserServlet" method="POST" style="display: none;">
                                    <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                                    <input type="hidden" name="action" value="<%= user.isDeactivated() ? "activate" : "deactivate" %>">
                                </form>
                            </div>
                            <div class="button-row">
                                <form action="DeleteUserServlet" method="POST" style="display: inline;" 
                                      onsubmit="return confirm('Are you sure you want to delete this user? This action cannot be undone.');">
                                    <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                                    <button type="submit" class="btn btn-danger">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </form>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </div>

</body>

</html>
