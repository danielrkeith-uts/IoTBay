<h1>Registered Customers</h1>

<table>
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
                    <a href="editCustomer.jsp?id=${customer.userId}">Edit</a> |
                    <a href="deactivateCustomer?id=${customer.userId}">Deactivate</a> |
                    <a href="viewAccessLogs?id=${customer.userId}">View Logs</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
