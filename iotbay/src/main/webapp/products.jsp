<%@ page import="model.Product, model.dao.ProductDBManager, java.util.List, model.Staff" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/ConnServlet" flush="true"/>
<%
    ProductDBManager productManager = (ProductDBManager) session.getAttribute("productDBManager");
    if (productManager == null) {
        out.println("<p>Product manager is not available. Please <a href='ConnServlet'>connect</a>.</p>");
        return;
    }
    String query = request.getParameter("query");
    List<Product> products = productManager.getAllProducts();

    if (query != null && !query.trim().isEmpty()) {
        String finalQuery = query.toLowerCase(); 
        products = products.stream()
            .filter(p -> p.getName().toLowerCase().contains(finalQuery) || 
                         p.getType().name().toLowerCase().contains(finalQuery))
            .collect(java.util.stream.Collectors.toList()); 
    }
%>

<!DOCTYPE html>
<html lang="en"> 
<head>
    <meta charset="UTF-8" />
    <title>Products</title>
    <link rel="stylesheet" href="main.css" />
    <link rel="stylesheet" href="products.css" />
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
            <a href="products.jsp" class="active">Products</a>
            
            <%
                model.User user = (model.User) session.getAttribute("user");
                boolean isStaff = (user != null && user instanceof Staff);
                
                if (user == null) {
            %>
                <a href="login.jsp">Login</a>
            <%
                } else {
            %>
            <% if (isStaff) { %>
                        <a href="adminInventory.jsp">Manage Inventory</a>
                    <% } %>
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
            <%
                }
            %>
        </navbar>
    </div>

    <main class="container mt-5">
        <h1 class="mb-4">Our Products</h1>
        <form class="mb-4" method="get" action="products.jsp">
            <div class="input-group">
                <input type="text" name="query" class="form-control" placeholder="Search by name or type" value="<%= request.getParameter("query") != null ? request.getParameter("query") : "" %>" />
                <button type="submit" class="btn btn-outline-secondary">Search</button>
            </div>
        </form>
        <% if (products.isEmpty()) { %>
            <div class="alert alert-secondary" role="alert">
                No matching products found. Please try broadening your search.
            </div>
        <% } else { %>
        <div class="row">
            <% for (Product p : products) { %>
                <div class="col-md-4 mb-4">
                    <div class="card product-card p-3 h-100">
                        <img src="<%= p.getImageUrl() != null && !p.getImageUrl().isEmpty() ? p.getImageUrl() : "https://png.pngtree.com/png-vector/20221125/ourmid/pngtree-no-image-available-icon-flatvector-illustration-blank-avatar-modern-vector-png-image_40962406.jpg" %>" class="card-img-top" alt="<%= p.getName() %>" style="height: 200px; object-fit: contain;" />
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title"><%= p.getName() %></h5>
                            <p class="card-text flex-grow-1"><%= p.getDescription() == null ? "" : p.getDescription()%></p>
                            <p class="card-text">
                                <strong>Type:</strong> 
                                <%= p.getType().name().substring(0,1).toUpperCase() + p.getType().name().substring(1).toLowerCase() %>
                            </p>
                            <p class="card-text">
                                <strong>Stock:</strong> 
                                <%= p.getStock() > 0 ? p.getStock() + " available" : "Out of stock" %>
                            </p>
                            <p class="card-text fw-bold">$<%= String.format("%.2f", p.getCost()) %></p>
                            <button 
                                class="btn btn-primary mt-auto <%= p.getStock() == 0 ? "disabled-button" : "" %>" 
                                type="button" 
                                <%= p.getStock() == 0 ? "disabled" : "" %>
                            >
                                Add to Cart
                            </button>
                        </div>
                    </div>
                </div>
            <% } %> 
        </div>
        <% } %>
    </main>
</body>
</html>
