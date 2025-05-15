<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> --%>
<%@ page import="java.util.List, java.util.ArrayList, model.Customer"%>
<jsp:include page="/ConnServlet" flush="true"/>
<jsp:include page="/CustomerListServlet" flush="true"/>
<%
    List<Customer> customers = (ArrayList<Customer>) request.getAttribute("customers");
%>
<%-- <p>Customers size: ${fn:length(customers)}</p> --%>

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
<% for (Customer customer : customers) { %>
            <tr>
                <td><%= customer.getFirstName() %></td>
                <td><%= customer.getLastName() %></td>
                <td><%= customer.getEmail() %></td>
                <td><%= customer.getPhone() %></td>
                <td>
                    <%-- <a href="editCustomer.jsp?id=${allCustomer.userId}">Edit</a> |
                    <a href="deactivateCustomer?id=${allCustomer.userId}">Deactivate</a> |
                    <a href="viewAccessLogs?id=${custoallCustomermer.userId}">View Logs</a> --%>
                </td>
            </tr>
<% } %>
</tbody>
</table>
