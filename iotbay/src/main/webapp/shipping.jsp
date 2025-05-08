<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        String error = (String) session.getAttribute("loginError");
        session.removeAttribute("loginError");
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
                <a href="login.jsp">Login</a>
            </navbar>
        </div>
                <!-- Simple Search Form -->
        <div class="mb-4">
            <h3>Search Shipments</h3>
            <form action="#" method="get">
                <div class="row">
                    <div class="col-md-5 mb-3">
                        <label for="shipmentId" class="form-label">Shipment ID</label>
                        <input type="text" id="shipmentId" name="shipmentId" class="form-control" />
                    </div>
                    <div class="col-md-5 mb-3">
                        <label for="shipmentDate" class="form-label">Shipment Date</label>
                        <input type="date" id="shipmentDate" name="shipmentDate" class="form-control" />
                    </div>
                    <div class="col-md-2 mb-3">
                        <label class="form-label">&nbsp;</label>
                        <input type="submit" value="Search" class="btn-green form-control" />
                    </div>
                </div>
            </form>
        </div>
        <!-- Basic Create Shipment Form -->
        <div class="mb-4">
            <h3>Create Shipment</h3>
            <form action="#" method="post">
                <div class="mb-3">
                    <label for="orderId" class="form-label">Order ID</label>
                    <input type="text" id="orderId" name="orderId" class="form-control" required />
                </div>
                
                <div class="mb-3">
                    <label for="shipmentMethod" class="form-label">Shipment Method</label>
                    <select id="shipmentMethod" name="shipmentMethod" class="form-control" required>
                        <option value="Standard">Standard</option>
                        <option value="Express">Express</option>
                        <option value="Priority">Priority</option>
                    </select>
                </div>
                
                <div class="mb-3">
                    <label for="shipmentDate" class="form-label">Shipment Date</label>
                    <input type="date" id="shipmentDate" name="shipmentDate" class="form-control" required />
                </div>
                
                <div class="mb-3">
                    <label for="address" class="form-label">Shipping Address</label>
                    <textarea id="address" name="address" class="form-control" rows="3" required></textarea>
                </div>
                
                <p class="error"><%= (error == null ? "" : error) %></p>
                
                <input type="submit" value="Create Shipment" class="btn-green" />
            </form>
        </div>
        <!-- Simple Shipment Table -->
        <div>
            <h3>My Shipments</h3>
            <table class="table">
                <thead>
                    <tr>
                        <th>Shipment ID</th>
                        <th>Order ID</th>
                        <th>Method</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>SH001</td>
                        <td>ORD123</td>
                        <td>Standard</td>
                        <td>2025-05-10</td>
                        <td>Processing</td>
                        <td>
                            <a href="#" class="btn-green">Edit</a>
                            <a href="#" class="btn-red">Delete</a>
                        </td>
                    </tr>
                    <tr>
                        <td>SH002</td>
                        <td>ORD456</td>
                        <td>Express</td>
                        <td>2025-05-08</td>
                        <td>Finalized</td>
                        <td>
                            <a href="#">View</a>
                        </td>
                    </tr>
                    <tr>
                        <td>SH003</td>
                        <td>ORD789</td>
                        <td>Priority</td>
                        <td>2025-05-15</td>
                        <td>Processing</td>
                        <td>
                            <a href="#" class="btn-green">Edit</a>
                            <a href="#" class="btn-red">Delete</a>
                        </td>
                    </tr>
                </tbody>
            </table>
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
        </style>
        </body>
        </html>
    </body>
</html>
