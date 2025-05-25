<%@ page import="java.util.List, java.util.ArrayList, model.Customer, model.User" %>
<%
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");

    if (customers == null) {
        customers = new ArrayList<>(); 
    }

    User loggedInUser = (User) session.getAttribute("user");
    
    boolean isAdmin = loggedInUser != null && loggedInUser.getRole() == User.Role.ADMIN; 
    boolean isStaff = loggedInUser != null && loggedInUser.getRole() == User.Role.STAFF; 
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registered Customers</title>

    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/customerlist.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</head>

<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <jsp:include page="navbar.jsp" />
    </div>

    <div class="customer-section">
        <h1>Registered Customers</h1>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
            <div class="d-flex gap-2">
                <button class="btn-green" onclick="window.location.href='addcustomer.jsp'">
                    <i class="fas fa-user-plus"></i> Add New Customer
                </button>
                <button class="btn-green" onclick="window.location.href='addaccesslogform.jsp'">
                    <i class="fas fa-clipboard-list"></i> Add Logs
                </button>
            </div>
            <form method="get" action="CustomerListServlet" class="d-flex gap-2">
                <input type="text" name="searchName" placeholder="Search by name" 
                    value="<%= request.getParameter("searchName") != null ? request.getParameter("searchName") : "" %>" 
                    class="form-control" />
                <select name="searchType" class="form-select">
                    <option value="">All Types</option>
                    <option value="INDIVIDUAL" <%= "INDIVIDUAL".equals(request.getParameter("searchType")) ? "selected" : "" %>>Individual</option>
                    <option value="COMPANY" <%= "COMPANY".equals(request.getParameter("searchType")) ? "selected" : "" %>>Company</option>
                </select>
                <button type="submit" class="btn-custom">Filter</button>
            </form>
        </div>
        
        <div class="customer-grid">
    <% for (Customer customer : customers) { 
        boolean isDeactivated = customer.isDeactivated();
    %>
    
        <div class="customer-card <%= isDeactivated ? "deactivated" : "" %>">
            <div class="content-wrapper">
                <p><strong>ID:</strong> <%= customer.getUserId() %></p>
                <p><strong>First Name:</strong> <%= customer.getFirstName() %></p>
                <p><strong>Last Name:</strong> <%= customer.getLastName() %></p>
                <p><strong>Email:</strong> <%= customer.getEmail() %></p>
                <p><strong>Phone:</strong> <%= customer.getPhone() %></p>
                <p><strong>Status:</strong> <%= isDeactivated ? "Deactivated" : "Active" %></p>
                <p><strong>Type:</strong> <%= customer.getType().name() %></p>
            </div>
            <div class="actions">
                <button 
                    onclick="window.location.href='LoadEditCustomerServlet?id=<%= customer.getUserId() %>'" 
                    <%= (isAdmin || isStaff) && !isDeactivated ? "" : "disabled class='btn btn-secondary'" %>>
                    Edit
                </button>

                <% if (isDeactivated) { %>
             <button 
                  <%= (isAdmin || isStaff) 
                      ? "onclick=\"window.location.href='reactivateCustomer?id=" + customer.getUserId() + "'\"" 
                     : "disabled class='btn btn-secondary'" %> 
                     class="btn btn-reactivate">
                         Reactivate
             </button>
        <% } else { %>
              <button 
                      <%= (isAdmin || isStaff) 
                     ? "onclick=\"window.location.href='deactivateCustomer?id=" + customer.getUserId() + "'\"" 
                    : "disabled class='btn btn-secondary'" %> 
                     class="btn btn-secondary">
                      Deactivate
            </button>
        <% } %>
                <button 
                    onclick="window.location.href='/iotbay/ApplicationAccessLogServlet?customer_id=<%= customer.getUserId() %>'" 
                    <%= (isAdmin || isStaff) ? "" : "disabled class='btn btn-secondary'" %>>
                    View Logs
                </button>
            </div>
          <button onclick="window.location.href='confirmdeletecustomer.jsp?userId=<%= customer.getUserId() %>'" class="btn btn-danger">Delete</button>

        </div>
    <% } %>
</div>

    </div>

</body>

</html>
