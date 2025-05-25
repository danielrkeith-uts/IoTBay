<%@ page import="model.User, model.Enums.ApplicationAction, model.dao.UserDBManager, java.util.List, model.Customer" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF)) {
        response.sendRedirect("unauthorized.jsp");
        return;
    }
    
    UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
    List<Customer> customers = userDBManager.getAllCustomers();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Access Log</title>
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet" href="css/addaccesslogform.css" />
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

    <div class="form-container">
        <h1>Add Access Log</h1>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <form action="AddAccessLogServlet" method="post">
            <div class="mb-3">
                <label for="userId" class="form-label">Select User:</label>
                <select name="userId" id="userId" class="form-select" required>
                    <option value="">Choose a user...</option>
                    <% for (Customer c : customers) { %>
                        <option value="<%= c.getUserId() %>">
                            <%= c.getFirstName() %> <%= c.getLastName() %> (ID: <%= c.getUserId() %>)
                        </option>
                    <% } %>
                </select>
            </div>

            <div class="mb-3">
                <label for="applicationAction" class="form-label">Action:</label>
                <select name="applicationAction" id="applicationAction" class="form-select" required>
                    <option value="LOGIN">LOGIN</option>
                    <option value="LOGOUT">LOGOUT</option>
                    <option value="REGISTER">REGISTER</option>
                    <option value="ADD_TO_CART">ADD TO CART</option>
                    <option value="PLACE_ORDER">PLACE ORDER</option>
                </select>
            </div>
            
            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-primary">Add Log</button>
                <button type="button" class="btn btn-secondary ms-2" onclick="goBack()">
                    <i class="fas fa-arrow-left"></i> Back
                </button>
            </div>
        </form>
    </div>
</body>
</html>
