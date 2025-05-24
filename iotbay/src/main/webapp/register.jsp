<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.User" %>
<!DOCTYPE html>
<html lang="en">
  <jsp:include page="/ConnServlet" flush="true"/>
  <%
    String error = (String) session.getAttribute("registerError");
    session.removeAttribute("registerError");
  %>
  <head>
    <meta charset="UTF-8"/>
    <title>Register</title>
    <link rel="stylesheet" href="main.css" />
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <script src="register.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
  </head>
  <body>
    <div class="banner">
      <h1>Internet of Things Store</h1>
      <navbar>
        <a href="index.jsp">Home</a>
        <a href="products.jsp">Products</a>
        <a href="login.jsp">Login</a>
        <a href="cart.jsp" class="bi bi-cart"></a>
      </navbar>
    </div>
    <div class="content container py-5">
      <h2 class="mb-4">Register</h2>
      <form action="RegisterServlet" method="post" class="row g-3">
        <div class="col-md-6">
          <label class="form-label">Email</label>
          <input type="text" name="email" class="form-control" required/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Password</label>
          <input type="password" name="password" class="form-control" required/>
          <div class="form-text">
            At least 8 chars, upper & lower case, number & special char
          </div>
        </div>
        <div class="col-md-6">
          <label class="form-label">First Name (optional)</label>
          <input type="text" name="firstName" class="form-control"/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Last Name (optional)</label>
          <input type="text" name="lastName" class="form-control"/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Phone Number (optional)</label>
          <input type="text" name="phone" class="form-control"/>
        </div>
        <div class="col-md-6 d-flex align-items-center">
          <div class="form-check">
            <input id="staff-checkbox" type="checkbox" name="isStaff"
                   class="form-check-input" onclick="handleStaffCheckbox()"/>
            <label for="staff-checkbox" class="form-check-label">
              Register as staff
            </label>
          </div>
        </div>

        <div id="staff-section" class="w-100" style="display:none">
          <div class="row g-3">
            <div class="col-md-4">
              <label class="form-label">Staff Card ID</label>
              <input type="text" name="staffCardId" class="form-control"/>
            </div>
            <div class="col-md-4">
              <label class="form-label">Admin Password</label>
              <input type="password" name="adminPassword" class="form-control"/>
            </div>
            <div class="col-md-4">
              <label class="form-label">Position</label>
              <select name="position" class="form-select">
                <option value="">-- choose one --</option>
                <option value="TECH">Tech</option>
                <option value="SALES">Sales</option>
                <option value="MANAGER">Manager</option>
              </select>
            </div>
          </div>
        </div>

        <div class="col-12">
          <p class="text-danger mb-3"><%= (error == null ? "" : error) %></p>
          <button type="submit" class="btn btn-success">Submit</button>
        </div>
      </form>
    </div>
  </body>
</html>
