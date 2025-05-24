<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="model.User,model.Staff" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    User user = (User) session.getAttribute("user");
    boolean isAdmin = (user instanceof Staff) && ((Staff)user).isAdmin();
    if (!isAdmin) {
        response.sendError(403, "Forbidden");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Edit Staff</title>
  <link rel="stylesheet" href="main.css"/>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
</head>
<body>
  <div class="banner">
    <h1>Internet of Things Store</h1>
    <navbar>
      <a href="index.jsp">Home</a>
      <a href="products.jsp">Products</a>
      <a href="StaffListServlet" class="active">Staff</a>
      <c:choose>
        <c:when test="${user == null}">
          <a href="login.jsp">Login</a>
        </c:when>
        <c:otherwise>
          <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown"
               href="#" role="button" aria-expanded="false">My Account</a>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
              <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
              <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
              <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
              <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
            </ul>
          </div>
        </c:otherwise>
      </c:choose>
    </navbar>
  </div>

  <div class="content container py-5">
    <h2>Edit Staff Member</h2>

    <c:if test="${not empty error}">
      <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
      <div class="alert alert-success">${success}</div>
    </c:if>

    <form action="EditStaffServlet" method="post" class="row g-3">
      <input type="hidden" name="userId" value="${staff.userId}"/>

      <div class="col-md-6">
        <label class="form-label">First Name</label>
        <input type="text" name="firstName"
               value="${staff.firstName}" class="form-control" required/>
      </div>
      <div class="col-md-6">
        <label class="form-label">Last Name</label>
        <input type="text" name="lastName"
               value="${staff.lastName}" class="form-control" required/>
      </div>
      <div class="col-md-6">
        <label class="form-label">Email</label>
        <input type="email" name="email"
               value="${staff.email}" class="form-control" required/>
      </div>
      <div class="col-md-6">
        <label class="form-label">Phone</label>
        <input type="text" name="phone"
               value="${staff.phone}" class="form-control"/>
      </div>
      <div class="col-md-6">
        <label class="form-label">Password</label>
        <input type="password" name="password"
               placeholder="Leave blank to keep existing" class="form-control"/>
      </div>
      <div class="col-md-6">
        <label class="form-label">Staff Card ID</label>
        <input type="number" name="staffCardId"
               value="${staff.staffCardId}" class="form-control" required/>
      </div>
      <div class="col-md-6">
        <label class="form-label">Position</label>
        <select name="position" class="form-select" required>
          <option value="TECH"    <c:if test="${staff.position=='TECH'}">selected</c:if>>Tech</option>
          <option value="SALES"   <c:if test="${staff.position=='SALES'}">selected</c:if>>Sales</option>
          <option value="MANAGER" <c:if test="${staff.position=='MANAGER'}">selected</c:if>>Manager</option>
        </select>
      </div>
      <div class="col-12">
        <button class="btn btn-primary">Update Staff</button>
        <a href="StaffListServlet" class="btn btn-secondary">Cancel</a>
      </div>
    </form>
  </div>
</body>
</html>


