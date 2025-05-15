<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<p>Customers size: ${fn:length(customers)}</p>

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
                <td>${cstomer.phone}</td>
                <td>
                    <%-- <a href="editCustomer.jsp?id=${allCustomer.userId}">Edit</a> |
                    <a href="deactivateCustomer?id=${allCustomer.userId}">Deactivate</a> |
                    <a href="viewAccessLogs?id=${custoallCustomermer.userId}">View Logs</a> --%>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
