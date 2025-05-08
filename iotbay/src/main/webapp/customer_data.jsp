<%@ page import="model.Customer" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Customer Data</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
    <h1>Registered Customers</h1>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

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
            <c:forEach var="customer" items="${customers}">
                <tr>
                    <td>${customer.firstName}</td>
                    <td>${customer.lastName}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>
                        <a href="editCustomer.jsp?id=${customer.userId}">Edit</a>
                        <a href="deactivateCustomer.jsp?id=${customer.userId}">Deactivate</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
