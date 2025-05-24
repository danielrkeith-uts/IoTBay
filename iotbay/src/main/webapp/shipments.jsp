<%-- shipments.jsp --%>
<%@ page import="java.util.List, model.User, model.Customer, model.Shipment"%>
<html>
    <jsp:include page="/ViewShipmentsServlet" flush="true"/>
    <%
        if (!(session.getAttribute("user") instanceof Customer)) {
            response.sendRedirect("index.jsp");
            return;
        }

        Customer customer = (Customer) session.getAttribute("user");
        List<Shipment> shipments = customer.getShipments();
        
        String error = (String) session.getAttribute("deleteShipmentError");
        session.removeAttribute("deleteShipmentError");
    %>
    <head>
        <link rel="stylesheet" href="css/main.css" />
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
        <div class="content">
            <h2>My Shipments</h2>
            
            <div class="mb-4">
                <form action="shipments.jsp" method="get" class="row g-3">
                    <div class="col-auto">
                        <input type="text" name="searchQuery" class="form-control" placeholder="Search by ID">
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn-green">Search</button>
                    </div>
                </form>
            </div>
            
            <a href="createshipment.jsp" class="btn-green mb-3">Create New Shipment</a>
            
            <% if (error != null && !error.isEmpty()) { %>
                <div class="alert alert-danger" role="alert">
                    <%= error %>
                </div>
            <% } %>
            
            <% if (shipments.isEmpty()) { %>
                <p>No shipments found.</p>
            <% } else { %>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Order ID</th>
                            <th>Method</th>
                            <th>Destination</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Shipment shipment : shipments) { %>
                            <tr>
                                <td><%= shipment.getShipmentId() %></td>
                                <td><%= shipment.getOrder().getOrderId() %></td>
                                <td><%= shipment.getMethod() %></td>
                                <td>
                                    <%= shipment.getAddress().getStreetNumber() %> 
                                    <%= shipment.getAddress().getStreet() %>, 
                                    <%= shipment.getAddress().getSuburb() %>, 
                                    <%= shipment.getAddress().getState() %> 
                                    <%= shipment.getAddress().getPostcode() %>
                                </td>
                                <td><%= shipment.getShipmentDate() %></td>
                                <td>
                                    <a href="editshipment.jsp?shipmentId=<%= shipment.getShipmentId() %>" class="btn btn-sm btn-primary">Edit</a>
                                    <a href="DeleteShipmentServlet?shipmentId=<%= shipment.getShipmentId() %>" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this shipment?')">Delete</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </body>
</html>