<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<p>Customers size: ${fn:length(allCustomer)}</p>


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
        <c:forEach var="allCustomer" items="${allCustomer}">
            <tr>
                <td>${allCustomer.firstName}</td>
                <td>${allCustomer.lastName}</td>
                <td>${allCustomer.email}</td>
                <td>${allCustomer.phone}</td>
                <td>
                    <!-- The links will need to be mapped properly, like so -->
                    <a href="editCustomer.jsp?id=${allCustomer.userId}">Edit</a> |
                    <a href="deactivateCustomer?id=${allCustomer.userId}">Deactivate</a> |
                    <a href="viewAccessLogs?id=${custoallCustomermer.userId}">View Logs</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
