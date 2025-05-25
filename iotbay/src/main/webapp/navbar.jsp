<%@ page import="model.User, model.Staff" %>
<%
    User user        = (User) session.getAttribute("user");
    boolean isLoggedIn = user != null;
    boolean isStaff    = isLoggedIn && user instanceof Staff;
    boolean isAdmin    = isStaff    && ((Staff) user).isAdmin();

    String currentPage = request.getRequestURI()
                              .substring(request.getRequestURI().lastIndexOf("/") + 1);
%>
<navbar>
  <a href="index.jsp"
     class="<%= currentPage.equals("index.jsp") ? "active" : "" %>">
    Home
  </a>
  <a href="products.jsp"
     class="<%= currentPage.equals("products.jsp") ? "active" : "" %>">
    Products
  </a>

  <% if (isStaff) { %>
    <a href="adminInventory.jsp"
       class="<%= currentPage.equals("adminInventory.jsp") ? "active" : "" %>">
      Manage Inventory
    </a>
  <% } %>

  <% if (isAdmin) { %>
    <a href="CustomerListServlet"
       class="<%= currentPage.equals("CustomerListServlet") ? "active" : "" %>">
      Customers
    </a>
    <a href="StaffListServlet"
       class="<%= currentPage.equals("StaffListServlet") ? "active" : "" %>">
      Staff
    </a>
  <% } %>

    <% if (!isLoggedIn) { %>
        <a href="login.jsp" class="<%= currentPage.equals("login.jsp") ? "active" : "" %>">Login</a>
    <% } else { %>
        <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle <%= currentPage.matches("account.jsp|shipments.jsp|applicationaccesslogs.jsp|deleteaccount.jsp") ? "active" : "" %>" 
               data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">
                My Account
            </a>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item <%= currentPage.equals("account.jsp") ? "active" : "" %>" href="account.jsp">Account Details</a></li>
                <li><a class="dropdown-item <%= currentPage.equals("myorders.jsp") ? "active" : "" %>" href="account.jsp">My Orders</a></li>
               <% if (!isStaff || !isLoggedIn) { %>
    <li><a class="dropdown-item <%= currentPage.equals("shipments.jsp") ? "active" : "" %>" href="shipments.jsp">My Shipments</a></li>
<% } %>
                <li><a class="dropdown-item <%= currentPage.equals("applicationaccesslogs.jsp") ? "active" : "" %>" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                <li><a class="dropdown-item <%= currentPage.equals("logout.jsp") ? "active" : "" %>" href="logout.jsp">Logout</a></li>
                <li><a class="dropdown-item text-danger <%= currentPage.equals("deleteaccount.jsp") ? "active" : "" %>" href="deleteaccount.jsp">Delete Account</a></li>
            </ul>
        </div>
    <% } %>

  <a href="cart.jsp"
     class="bi bi-cart
            <%= currentPage.equals("cart.jsp") ? "active" : "" %>">
  </a>
</navbar>
