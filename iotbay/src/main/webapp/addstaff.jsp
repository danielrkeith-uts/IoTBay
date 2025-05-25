<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User,model.Staff" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Add Staff</title>
  <link rel="stylesheet" href="css/main.css"/>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"/>
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"/>
</head>
<body>
  <div class="banner">
    <h1>Internet of Things Store</h1>
    <jsp:include page="navbar.jsp"/>
  </div>

  <div class="content container py-5">
    <h2 class="mb-4">Add New Staff Member</h2>
    <c:if test="${not empty success}">
      <div class="alert alert-success">
        ${success}
        <a href="StaffListServlet" class="btn btn-sm btn-outline-primary mt-2">← Back to List</a>
      </div>
    </c:if>
    <c:if test="${empty success}">
      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>
      <form action="AddStaffServlet" method="post" class="row g-3">
        <div class="col-md-6">
          <label class="form-label">First Name</label>
          <input name="firstName" class="form-control" required/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Last Name</label>
          <input name="lastName" class="form-control" required/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Email</label>
          <input type="email" name="email" class="form-control" required/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Phone</label>
          <input name="phone" class="form-control"/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Password</label>
          <input type="password" name="password" class="form-control" required/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Staff Card ID</label>
          <input type="number" name="staffCardId" class="form-control" required/>
        </div>
        <div class="col-md-6">
          <label class="form-label">Position</label>
          <select name="position" class="form-select" required>
            <option value="">– choose one –</option>
            <option value="TECH">Tech</option>
            <option value="SALES">Sales</option>
            <option value="MANAGER">Manager</option>
          </select>
        </div>
        <div class="col-12">
          <button class="btn btn-success">Create Staff</button>
          <a href="StaffListServlet" class="btn btn-secondary">Cancel</a>
        </div>
      </form>
    </c:if>
  </div>
</body>
</html>

