<%-- createshipment.jsp --%>
<%@ page import="model.User, model.Customer, model.Enums.AuState"%>
<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        if (!(session.getAttribute("user") instanceof Customer)) {
            response.sendRedirect("index.jsp");
            return;
        }

        String error = (String) session.getAttribute("shipmentError");
        session.removeAttribute("shipmentError");
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
            <h2>Create Shipment</h2>
            
            <% if (error != null && !error.isEmpty()) { %>
                <div class="alert alert-danger" role="alert">
                    <%= error %>
                </div>
            <% } %>
            
            <form action="CreateShipmentServlet" method="post">
                <div class="mb-3">
                    <label for="orderId" class="form-label">Order ID</label>
                    <input type="number" name="orderId" class="form-control" required />
                </div>
                
                <h4>Shipping Address</h4>
                
                <div class="mb-3">
                    <label for="streetNumber" class="form-label">Street Number</label>
                    <input type="number" name="streetNumber" class="form-control" required />
                </div>
                
                <div class="mb-3">
                    <label for="street" class="form-label">Street</label>
                    <input type="text" name="street" class="form-control" required />
                </div>
                
                <div class="mb-3">
                    <label for="suburb" class="form-label">Suburb</label>
                    <input type="text" name="suburb" class="form-control" required />
                </div>
                
                <div class="mb-3">
                    <label for="state" class="form-label">State</label>
                    <select name="state" class="form-control" required>
                        <% for (AuState state : AuState.values()) { %>
                            <option value="<%= state.name() %>"><%= state.name() %></option>
                        <% } %>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="postcode" class="form-label">Postcode</label>
                    <input type="text" name="postcode" class="form-control" pattern="[0-9]{4}" maxlength="4" required />
                    <small class="form-text text-muted">4-digit postcode</small>
                </div>
                
                <div class="mb-3">
                    <label for="method" class="form-label">Shipping Method</label>
                    <select name="method" class="form-control" required>
                        <option value="Standard">Standard (3-5 business days)</option>
                        <option value="Express">Express (1-2 business days)</option>
                        <option value="Priority">Priority (Next day)</option>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="shipmentDate" class="form-label">Shipment Date</label>
                    <input type="date" name="shipmentDate" class="form-control" required />
                </div>
                
                <div class="mb-3">
                    <a href="shipments.jsp" class="btn btn-secondary">Cancel</a>
                    <input type="submit" class="btn-green" value="Create Shipment" />
                </div>
            </form>
        </div>
    </body>
</html>