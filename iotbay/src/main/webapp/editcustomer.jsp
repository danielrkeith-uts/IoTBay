<%@ page import="model.Customer" %>
<%
    Customer customer = (Customer) request.getAttribute("customer");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Customer Details</title>

    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet" href="css/addcustomer.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
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
                    <li><a class="dropdown-item" href="customerlist.jsp">Customers</a></li>
                    <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                </ul>
            </div>
        </navbar>
    </div>

    <div class="container d-flex justify-content-center mt-5">
        <div class="form-container">
            <h2>Edit Customer Details</h2>

            <% if (customer == null) { %>
                <p class="error">Customer not found or an error occurred.</p>
            <% } else { %>
                <form action="EditCustomerServlet" method="post">
                    <input type="hidden" name="userId" value="<%= customer.getUserId() %>" />
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="text" id="email" name="email" value="<%= customer.getEmail() %>" disabled class="form-control" />
                    </div>
                    <div class="form-group">
                        <label for="firstName">First Name:</label>
                        <input type="text" id="firstName" name="firstName" value="<%= customer.getFirstName() %>" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label for="lastName">Last Name:</label>
                        <input type="text" id="lastName" name="lastName" value="<%= customer.getLastName() %>" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone:</label>
                        <input type="text" id="phone" name="phone" value="<%= customer.getPhone() %>" class="form-control" />
                    </div>

                    <p class="error text-center"><%= (error == null ? "" : error) %></p>
                    <p class="success text-center"><%= (success == null ? "" : success) %></p>
                    <div class="text-center">
                        <input type="submit" value="Update Customer" class="btn-green" />
                    </div>
                </form>
            <% } %>

            <div class="text-center mb-4">
                <a href="customerlist.jsp" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back to Customer List
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</body>
</html>
