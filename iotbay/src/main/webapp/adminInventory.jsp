<%@ page import="model.User, model.Staff, model.Product, model.dao.ProductDBManager, java.util.List" %>
<%@ page session="true" %>

<%
   try {
       User user = (User) session.getAttribute("user");
       ProductDBManager productManager = (ProductDBManager) session.getAttribute("productDBManager");


       
      
       if (!(user instanceof Staff)) {
           out.println("You are not authorized to view this page.");
           return;
       }
      // ProductDBManager productManager = (ProductDBManager) session.getAttribute("productDBManager");
      // out.println("User: " + user);
      // out.println("ProductDBManager: " + productManager);
      
       if (productManager == null) {
           out.println("Product manager is not available. Please ensure the database connection is initialized.");
           return;
       }

       List<Product> products = productManager.getAllProducts();
       //out.println("Total products found: " + products.size() + "<br>");
       request.setAttribute("products", products);
%>
<!DOCTYPE html>
<html>
<head>
   <title>Admin Inventory</title>
   <link rel="stylesheet" href="css/main.css" />
   <link rel="stylesheet" href="css/index.css" />
   <link rel="stylesheet" href="css/products.css" />
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
           <a href="adminInventory.jsp" class="active">Manage Inventory</a>
           <div class="nav-item dropdown">
               <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
               <ul class="dropdown-menu">
                   <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                   <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                   <li><a class="dropdown-item" href="#">Application Access Logs</a></li>
                   <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                   <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
               </ul>
           </div>
       </navbar>
   </div>


   <div class="content container mt-5">
       <h2>Manage Products</h2>
       <div class="row">
           <% for (Product p : products) { %>
           <div class="col-md-4 mb-4">
               <div class="card product-card p-3">
                   <form action="<%=request.getContextPath()%>/ProductServlet" method="post">
                       <label>Product Name:</label>
                       <h5><input type="text" name="name" class="form-control mb-2" value="<%= p.getName() %>" required/></h5>
                       <label>Description:</label>
                       <input type="text" name="description" class="form-control mb-2" value="<%= p.getDescription() == null ? "" : p.getDescription() %>" />
                       <label>Type:</label>
                        <select name="type" class="form-control mb-2" required>
                            <option value="ELECTRONICS" <%= "ELECTRONICS".equals(p.getType().name()) ? "selected" : "" %>>Electronics</option>
                            <option value="ACCESSORY" <%= "ACCESSORY".equals(p.getType().name()) ? "selected" : "" %>>Accessory</option>
                            <option value="COMPONENTS" <%= "COMPONENTS".equals(p.getType().name()) ? "selected" : "" %>>Components</option>
                        </select>

                       <label>Price:</label>
                       <input type="number" name="cost" class="form-control mb-2" value="<%= p.getCost() %>" step="0.01" min="0" required/>
                       <label>Stock:</label>
                       <input type="number" name="stock" class="form-control mb-3" value="<%= p.getStock() %>" min="0" required/>
                       <input type="hidden" name="originalName" value="<%= p.getName() %>" />
                       <label>Image URL:</label>
                       <input type="text" name="imageUrl" class="form-control mb-3" value="<%= p.getImageUrl() != null ? p.getImageUrl() : "" %>" placeholder="e.g. images/product1.jpg" />


                       <button class="btn btn-primary me-2" name="action" value="update">Update</button>
                       <button class="btn btn-danger" name="action" value="delete">Delete</button>
                   </form>
               </div>
           </div>
           <% } %>
       </div>


           <h3 class="mt-5">Add New Product</h3>
       <div class="col-md-4 mb-4">
           <div class="card product-card p-3">
               <form action="<%=request.getContextPath()%>/ProductServlet" method="post">
                   <label>Product Name:</label>
                   <h5><input type="text" name="name" class="form-control mb-2" placeholder="Product Name" required/></h5>
                   <label>Description:</label>
                   <input type="text" name="description" class="form-control mb-2" placeholder="Description (optional)" />
                   <label>Type:</label>
                    <select name="type" class="form-control mb-2" required>
                        <option value="" disabled selected>Select a type</option>
                        <option value="ELECTRONICS">Electronics</option>
                        <option value="ACCESSORY">Accessory</option>
                        <option value="COMPONENTS">Components</option>
                    </select>
                   <label>Price:</label>
                   <input type="number" name="cost" class="form-control mb-2" placeholder="Price" step="0.01" min="0" required/>
                   <label>Stock:</label>
                   <input type="number" name="stock" class="form-control mb-3" placeholder="Stock" min="0" required/>
                   <label>Image URL:</label>
                   <input type="text" name="imageUrl" class="form-control mb-3" placeholder="Image URL (optional)" />
                   <button class="btn btn-primary me-2" name="action" value="add">Add Product</button>
               </form>
           </div>
       </div>


</body>

<%
   } catch (Exception e) {
       e.printStackTrace(new java.io.PrintWriter(out));
   }
%>
</html>