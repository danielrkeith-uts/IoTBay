<%@ page import="java.util.List, java.util.ArrayList, model.Customer, model.User" %>
<jsp:include page="/ConnServlet" flush="true"/>
<jsp:include page="/CustomerListServlet" flush="true"/>

<%
    List<Customer> customers = (ArrayList<Customer>) request.getAttribute("customers");
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

    <link rel="stylesheet" href="main.css"  />
    <link rel="stylesheet" href="customer-table.css"  />
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
                    <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                </ul>
            </div>
        </navbar>
    </div>

    <div class="customer-section">
        <h1>Registered Customers</h1>
        
        <div class="customer-grid">
    <% for (Customer customer : customers) { 
        boolean isDeactivated = customer.isDeactivated();
    %>
        <div class="customer-card <%= isDeactivated ? "deactivated" : "" %>">
            <div class="content-wrapper">
                <p><strong>First Name:</strong> <%= customer.getFirstName() %></p>
                <p><strong>Last Name:</strong> <%= customer.getLastName() %></p>
                <p><strong>Email:</strong> <%= customer.getEmail() %></p>
                <p><strong>Phone:</strong> <%= customer.getPhone() %></p>
                <p><strong>Status:</strong> <%= isDeactivated ? "Deactivated" : "Active" %></p>
            </div>
            <div class="actions">
               <button 
    onclick="window.location.href='LoadEditCustomerServlet?id=<%= customer.getUserId() %>'" 
    <%= (isAdmin || isStaff) ? "" : "disabled class='btn btn-secondary'" %>>
    Edit
</button>

                <button 
                    <%= isDeactivated 
                        ? "disabled class='btn btn-secondary'" 
                        : (isAdmin || isStaff ? "onclick=\"window.location.href='deactivateCustomer?id=" + customer.getUserId() + "'\"" : "disabled class='btn btn-secondary'") %>>
                    <%= isDeactivated ? "Deactivated" : (isAdmin || isStaff ? "Deactivate" : "No Permission") %>
                </button>

               <button 
                    onclick="window.location.href='/iotbay/ApplicationAccessLogServlet?customer_id=<%= customer.getUserId() %>'" 
                     <%= isDeactivated ? "disabled class='btn btn-secondary'" : (isStaff ? "" : "disabled class='btn btn-secondary'") %>>
                      View Logs
                </button>
            </div>
        </div>
    <% } %>
</div>

            <a href="addcustomer.jsp">Add New Customer</a>

    </div>

</body>

</html>
