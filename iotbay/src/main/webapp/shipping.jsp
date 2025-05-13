<%@ page import="model.User"%>
<%@ page import="model.Delivery"%>
<%@ page import="model.Enums.AuState"%>
<%@ page import="java.util.List"%>
<html>
<jsp:include page="/ConnServlet" flush="true"/>
<%
 User user = (User) session.getAttribute("user");
 String error = (String) session.getAttribute("shippingError");
 session.removeAttribute("shippingError");
 String successMessage = (String) session.getAttribute("successMessage");
 session.removeAttribute("successMessage");
 
 Delivery selectedShipment = (Delivery) request.getAttribute("selectedShipment");
 boolean viewOnly = request.getAttribute("viewOnly") != null && (boolean)request.getAttribute("viewOnly");
 
 // Get the list of shipments from the request
 List<Delivery> shipments = (List<Delivery>) request.getAttribute("shipments");
 %>
<head>
<link rel="stylesheet" href="main.css" />
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
<a href="shipping.jsp" class="active">Shipping</a>
<% if (user == null) { %>
    <a href="login.jsp">Login</a>
<% } else { %>
    <div class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
        </ul>
    </div>
<% } %>
</navbar>
</div>
<div class="content">
<h2>Shipping Management</h2>

<% if (successMessage != null && !successMessage.isEmpty()) { %>
<div class="alert alert-success alert-dismissible fade show" role="alert">
    <%= successMessage %>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
</div>
<% } %>

<!-- Basic Create/Update Shipment Form -->
<div class="mb-4">
    <h3><%= selectedShipment == null ? "Create Shipment" : (viewOnly ? "View Shipment" : "Update Shipment") %></h3>
    <form action="ShippingServlet" method="post">
        <% if (selectedShipment != null) { %>
            <input type="hidden" name="shipmentId" value="<%= selectedShipment.getDeliveryId() %>" />
        <% } %>
        
        <div class="mb-3">
            <label for="shipmentMethod" class="form-label">Shipment Method</label>
            <select id="shipmentMethod" name="shipmentMethod" class="form-control" required <%= viewOnly ? "disabled" : "" %>>
                <option value="Standard" <%= selectedShipment != null && "Australia Post".equals(selectedShipment.getCourier()) ? "selected" : "" %>>Standard</option>
                <option value="Express" <%= selectedShipment != null && "DHL".equals(selectedShipment.getCourier()) ? "selected" : "" %>>Express</option>
                <option value="Priority" <%= selectedShipment != null && "FedEx".equals(selectedShipment.getCourier()) ? "selected" : "" %>>Priority</option>
                <option value="Other" <%= selectedShipment != null && !("Australia Post".equals(selectedShipment.getCourier()) || "DHL".equals(selectedShipment.getCourier()) || "FedEx".equals(selectedShipment.getCourier())) ? "selected" : "" %>>Other</option>
            </select>
        </div>
        
        <div class="mb-3">
            <label for="address" class="form-label">Shipping Address</label>
            <textarea id="address" name="address" class="form-control" rows="3" required <%= viewOnly ? "disabled" : "" %>><%= 
                selectedShipment != null ? 
                selectedShipment.getDestination().getStreetNumber() + ", " + 
                selectedShipment.getDestination().getStreet() + ", " + 
                selectedShipment.getDestination().getSuburb() + ", " + 
                selectedShipment.getDestination().getState() + ", " + 
                selectedShipment.getDestination().getPostcode() : ""
            %></textarea>
            <small class="form-text text-muted">Format: street number, street, suburb, state, postcode</small>
        </div>
        
        <p class="error"><%= (error == null ? "" : error) %></p>
        
        <% if (selectedShipment == null) { %>
            <input type="submit" name="create" value="Create Shipment" class="btn-green" />
        <% } else if (!viewOnly) { %>
            <input type="submit" name="update" value="Update Shipment" class="btn-green" />
            <input type="submit" name="delete" value="Delete Shipment" class="btn-red" 
                  onclick="return confirm('Are you sure you want to delete this shipment?')" />
            <a href="shipping.jsp" class="btn-gray">Cancel</a>
        <% } else { %>
            <a href="shipping.jsp" class="btn-gray">Back to Shipments</a>
        <% } %>
    </form>
</div>

<!-- Real Shipments from Database -->
<div>
    <h3>My Shipments</h3>
    <% if (shipments == null || shipments.isEmpty()) { %>
        <p>No shipments found. Create a new shipment to get started.</p>
    <% } else { %>
        <table class="table">
            <thead>
                <tr>
                    <th>Shipment ID</th>
                    <th>Method</th>
                    <th>Destination</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Delivery shipment : shipments) { %>
                    <tr>
                        <td><%= shipment.getDeliveryId() %></td>
                        <td><%= 
                            "Australia Post".equals(shipment.getCourier()) ? "Standard" : 
                            "DHL".equals(shipment.getCourier()) ? "Express" : 
                            "FedEx".equals(shipment.getCourier()) ? "Priority" : 
                            shipment.getCourier() 
                        %></td>
                        <td><%= shipment.getDestination().getSuburb() + ", " + shipment.getDestination().getState() %></td>
                        <td>
                            <a href="ShippingServlet?action=edit&shipmentId=<%= shipment.getDeliveryId() %>" class="btn-green">Edit</a>
                            <a href="ShippingServlet?action=view&shipmentId=<%= shipment.getDeliveryId() %>">View</a>
                            <form action="ShippingServlet" method="post" style="display: inline;">
                                <input type="hidden" name="shipmentId" value="<%= shipment.getDeliveryId() %>" />
                                <input type="hidden" name="delete" value="true" />
                                <button type="submit" class="btn-red" onclick="return confirm('Are you sure you want to delete this shipment?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>

</div>
<style>
.btn-red {
    background-color: #d9534f;
    color: white;
    padding: 6px 12px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
}
.btn-gray {
    background-color: #6c757d;
    color: white;
    padding: 6px 12px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    text-decoration: none;
    display: inline-block;
}
</style>
</body>
</html>