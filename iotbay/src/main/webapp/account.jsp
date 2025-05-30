<%@ page import="model.User, model.Staff"%>
<html>
    <jsp:include page="/ConnServlet" flush="true"/>
  <%
    if (session.getAttribute("user") == null) {
      response.sendRedirect("index.jsp");
      return;
    }
    String error   = (String) session.getAttribute("accountError");
    session.removeAttribute("accountError");
    String success = (String) session.getAttribute("accountSuccess");
    session.removeAttribute("accountSuccess");
    User user = (User) session.getAttribute("user");
    boolean isStaff = user instanceof Staff;
    boolean isAdmin = isStaff && ((Staff)user).isAdmin();
  %>
  <head>
    <link rel="stylesheet" href="css/main.css" />
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
  </head>
  <body>
    <div class="banner">
      <h1>Internet of Things Store</h1>
      <navbar>
        <a href="index.jsp">Home</a>
        <a href="products.jsp">Products</a>
        <% if (isStaff) { %>
          <a href="adminInventory.jsp">Manage Inventory</a>
        <% } %>
        <% if (isAdmin) { %>
          <a href="StaffListServlet">Staff</a>
        <% } %>
        <div class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown"
             href="#" role="button" aria-expanded="false">My Account</a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                        <li><a class="dropdown-item" href="myorders.jsp">My Orders</a></li>
            <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
          </ul>
        </div>
        <a href="cart.jsp" class="bi bi-cart"></a>
      </navbar>
    </div>
    <div class="content">
      <h2>Account Details</h2>
      <form action="AccountDetailsServlet" method="post">
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input type="text" name="email" disabled class="form-control"
                 value="<%= user.getEmail() %>" />
        </div>
        <div class="mb-3">
          <label class="form-label">First Name</label>
          <input type="text" name="firstName" class="form-control"
                 value="<%= user.getFirstName() %>" />
        </div>
        <div class="mb-3">
          <label class="form-label">Last Name</label>
          <input type="text" name="lastName" class="form-control"
                 value="<%= user.getLastName() %>" />
        </div>
        <div class="mb-3">
          <label class="form-label">Phone Number</label>
          <input type="text" name="phone" class="form-control"
                 value="<%= user.getPhone() %>" />
        </div>
        <% if (isStaff) { %>
          <div class="mb-3">
            <label class="form-label">Staff Card ID</label>
            <input type="text" name="staffCardId" class="form-control"
                   value="<%= ((Staff)user).getStaffCardId() %>" />
          </div>
          <div class="mb-3">
            <label class="form-label">Position</label>
            <select name="position" class="form-select" required>
              <option value="TECH"
                <%= "TECH".equals(((Staff)user).getPosition()) ? "selected" : "" %>>
                Tech
              </option>
              <option value="SALES"
                <%= "SALES".equals(((Staff)user).getPosition()) ? "selected" : "" %>>
                Sales
              </option>
              <option value="MANAGER"
                <%= "MANAGER".equals(((Staff)user).getPosition()) ? "selected" : "" %>>
                Manager
              </option>
            </select>
          </div>
        <% } %>
        <p>Password: <a href="changepassword.jsp">Change password</a></p>
        <p class="error"><%= (error   == null ? "" : error)   %></p>
        <p class="success"><%= (success == null ? "" : success) %></p>
        <input type="submit" class="btn-green" value="Save changes" />
      </form>
    </div>
  </body>
</html>

