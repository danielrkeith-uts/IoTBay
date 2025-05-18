<%@ page import="model.Product, model.dao.ProductDBManager, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ProductDBManager productManager = (ProductDBManager) session.getAttribute("productDBManager");
    if (productManager == null) {
        out.println("<p>Product manager is not available. Please <a href='ConnServlet'>connect</a>.</p>");
        return;
    }
    List<Product> products = productManager.getAllProducts();
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
                if (user == null) {
            %>
                <a href="login.jsp">Login</a>
            <%
                } else {
            %>
            <%
                boolean isStaff = false;
                if (user instanceof model.Staff) {
                    isStaff = true;
                }
            %>
                <div class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                        <li><a class="dropdown-item" href="#">Application Access Logs</a></li>
                    <% if (isStaff) { %>
                        <li><a class="dropdown-item" href="adminInventory.jsp">Manage Inventory</a></li>
                    <% } %>
                        <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                    </ul>
                </div>
            <%
                }
            %>
        </navbar>
    </div>

    <main class="container mt-5">
        <h1 class="mb-4">Our Products</h1>
        <div class="row">
            <% for (Product p : products) { %>
                <div class="col-md-4 mb-4">
                    <div class="card product-card p-3 h-100">
                        <img src="<%= p.getImageUrl() != null && !p.getImageUrl().isEmpty() ? p.getImageUrl() : "images/default-product.png" %>" class="card-img-top" alt="<%= p.getName() %>" style="height: 200px; object-fit: contain;" />
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title"><%= p.getName() %></h5>
                            <p class="card-text flex-grow-1"><%= p.getDescription() %></p>
                            <p class="card-text fw-bold">$<%= String.format("%.2f", p.getCost()) %></p>
                            <button class="btn btn-primary mt-auto" type="button">Add to Cart</button>
                        </div>
                    </div>
                </div>
            <% } %>
        </div>
    </main>
</body>
</html>
