<%@ page import="model.User, model.Staff" %>
<%
    User user       = (User) session.getAttribute("user");
    boolean isLoggedIn = (user != null);
    boolean isStaff    = isLoggedIn && user instanceof Staff;
    boolean isAdmin    = isStaff && ((Staff)user).isAdmin();

    String uri = request.getRequestURI();
    String current = uri.substring(uri.lastIndexOf("/") + 1);
%>
<navbar>
  <a href="index.jsp"
     class="<%= "index.jsp".equals(current) ? "active" : "" %>">
    Home
  </a>
  <a href="products.jsp"
     class="<%= "products.jsp".equals(current) ? "active" : "" %>">
    Products
  </a>

  <%-- only admins get the Staff link --%>
  <% if (isAdmin) { %>
    <a href="StaffListServlet"
       class="<%= "StaffListServlet".equals(current) ? "active" : "" %>">
      Staff
    </a>
  <% } %>

  <% if (!isLoggedIn) { %>
    <a href="login.jsp"
       class="<%= "login.jsp".equals(current) ? "active" : "" %>">
      Login
    </a>
  <% } else { %>
    <div class="nav-item dropdown">
      <a class="nav-link dropdown-toggle
                <%= current.matches("account.jsp|shipments.jsp|applicationaccesslogs.jsp|deleteaccount.jsp") ? "active" : "" %>"
         href="#"
         data-bs-toggle="dropdown"
         role="button"
         aria-expanded="false">
        My Account
      </a>
      <ul class="dropdown-menu">
        <li>
          <a class="dropdown-item
                     <%= "account.jsp".equals(current) ? "active" : "" %>"
             href="account.jsp">
            Account Details
          </a>
        </li>
        <li>
          <a class="dropdown-item
                     <%= "shipments.jsp".equals(current) ? "active" : "" %>"
             href="shipments.jsp">
            My Shipments
          </a>
        </li>
        <li>
          <a class="dropdown-item
                     <%= "applicationaccesslogs.jsp".equals(current) ? "active" : "" %>"
             href="applicationaccesslogs.jsp">
            Access Logs
          </a>
        </li>
        <li>
          <a class="dropdown-item
                     <%= "logout.jsp".equals(current) ? "active" : "" %>"
             href="logout.jsp">
            Logout
          </a>
        </li>
        <li>
          <a class="dropdown-item text-danger
                     <%= "deleteaccount.jsp".equals(current) ? "active" : "" %>"
             href="deleteaccount.jsp">
            Delete Account
          </a>
        </li>
      </ul>
    </div>
  <% } %>

  <a href="cart.jsp"
     class="bi bi-cart <%= "cart.jsp".equals(current) ? "active" : "" %>">
  </a>
</navbar>

