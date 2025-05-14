<%@ page import="java.util.List" %>
<%@ page import="model.Customer" %>
<html>
<head>
    <title>Customer Data</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
    <h1>Registered Customers</h1>

    <%
        List<Customer> customers = (List<Customer>) request.getAttribute("customers");
        String error = (String) request.getAttribute("error");
    %>

    <%-- Debug: Output customer list --%>
    <p>Debug: Customer list = <%= customers != null ? customers.toString() : "null" %></p>
    <p>Debug: Customer size = <%= (customers != null ? customers.size() : "null") %></p>

    <% if (error != null && !error.isEmpty()) { %>
        <p class="error"><%= error %></p>
    <% } else if (customers == null || customers.isEmpty()) { %>
        <p>No customers found.</p>
    <% } else { %>
        <table border="1">
            <thead>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Customer customer : customers) { %>
                    <tr>
                        <td><%= customer.getFirstName() %></td>
                        <td><%= customer.getLastName() %></td>
                        <td><%= customer.getEmail() %></td>
                        <td><%= customer.getPhone() %></td>
                        <td>
                            <a href="editCustomer.jsp?id=<%= customer.getUserId() %>">Edit</a>
                            <a href="deactivateCustomer.jsp?id=<%= customer.getUserId() %>">Deactivate</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</body>
</html>
l