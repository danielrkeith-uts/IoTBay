<%-- editshipment.jsp --%>
<%@ page import="model.User, model.Customer, model.Shipment, model.dao.ShipmentDBManager, model.Enums.AuState"%>
<html>
    <%
        if (!(session.getAttribute("user") instanceof Customer)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String error = (String) session.getAttribute("updateShipmentError");
        session.removeAttribute("updateShipmentError");
        
        String shipmentIdStr = request.getParameter("shipmentId");
        int shipmentId = Integer.parseInt(shipmentIdStr);
        
        ShipmentDBManager shipmentDBManager = (ShipmentDBManager) session.getAttribute("shipmentDBManager");
        Shipment shipment = shipmentDBManager.getShipment(shipmentId);
        
        if (shipment == null) {
            response.sendRedirect("shipments.jsp");
            return;
        }
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
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                        <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                        <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                        <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                        <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                    </ul>
                </div>
            </navbar>
        </div>
        <div class="content">
            <h2>Edit Shipment</h2>
            
            <% if (error != null && !error.isEmpty()) { %>
                <div class="alert alert-danger" role="alert">
                    <%= error %>
                </div>
            <% } %>
            
            <form action="UpdateShipmentServlet" method="post">
                <input type="hidden" name="shipmentId" value="<%= shipment.getShipmentId() %>" />
                
                <div class="mb-3">
                    <label class="form-label">Order ID</label>
                    <input type="text" class="form-control" value="<%= shipment.getOrder().getOrderId() %>" disabled />
                </div>
                
                <h4>Shipping Address</h4>
                
                <div class="mb-3">
                    <label for="streetNumber" class="form-label">Street Number</label>
                    <input type="number" name="streetNumber" class="form-control" value="<%= shipment.getAddress().getStreetNumber() %>" required />
                </div>
                
                <div class="mb-3">
                    <label for="street" class="form-label">Street</label>
                    <input type="text" name="street" class="form-control" value="<%= shipment.getAddress().getStreet() %>" required />
                </div>
                
                <div class="mb-3">
                    <label for="suburb" class="form-label">Suburb</label>
                    <input type="text" name="suburb" class="form-control" value="<%= shipment.getAddress().getSuburb() %>" required />
                </div>
                
                <div class="mb-3">
                    <label for="state" class="form-label">State</label>
                    <select name="state" class="form-control" required>
                        <% for (AuState state : AuState.values()) { %>
                            <option value="<%= state.name() %>" <%= state == shipment.getAddress().getState() ? "selected" : "" %>><%= state.name() %></option>
                        <% } %>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="postcode" class="form-label">Postcode</label>
                    <input type="text" name="postcode" class="form-control" value="<%= shipment.getAddress().getPostcode() %>" pattern="[0-9]{4}" maxlength="4" required />
                    <small class="form-text text-muted">4-digit postcode</small>
                </div>
                
                <div class="mb-3">
                    <label for="method" class="form-label">Shipping Method</label>
                    <select name="method" class="form-control" required>
                        <option value="Standard" <%= "Standard".equals(shipment.getMethod()) ? "selected" : "" %>>Standard (3-5 business days)</option>
                        <option value="Express" <%= "Express".equals(shipment.getMethod()) ? "selected" : "" %>>Express (1-2 business days)</option>
                        <option value="Priority" <%= "Priority".equals(shipment.getMethod()) ? "selected" : "" %>>Priority (Next day)</option>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="shipmentDate" class="form-label">Shipment Date</label>
                    <input type="date" name="shipmentDate" class="form-control" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(shipment.getShipmentDate()) %>" required />
                </div>
                
                <div class="mb-3">
                    <a href="shipments.jsp" class="btn btn-secondary">Cancel</a>
                    <input type="submit" class="btn-green" value="Update Shipment" />
                </div>
            </form>
        </div>
    </body>
</html>