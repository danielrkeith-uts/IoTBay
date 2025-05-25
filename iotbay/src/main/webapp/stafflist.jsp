<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User,model.Staff" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>All Staff Members</title>
  <link rel="stylesheet" href="css/main.css"/>
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
    <jsp:include page="navbar.jsp"/>
  </div>

  <div class="content container py-5">
    <h2>All Staff Members</h2>

    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty success}">
      <div class="alert alert-success">${success}</div>
    </c:if>

    <!-- search + filter form -->
    <form method="get" action="StaffListServlet" class="row g-3 mb-4">
      <div class="col-md-5">
        <input
          type="text"
          name="q"
          value="${param.q}"
          class="form-control"
          placeholder="Search by name…"
        />
      </div>
      <div class="col-md-4">
        <select name="position" class="form-select">
          <option value=""  ${param.position==''    ? 'selected':''}>All Positions</option>
          <option value="TECH"    ${param.position=='TECH'    ? 'selected':''}>Tech</option>
          <option value="SALES"   ${param.position=='SALES'   ? 'selected':''}>Sales</option>
          <option value="MANAGER" ${param.position=='MANAGER' ? 'selected':''}>Manager</option>
        </select>
      </div>
      <div class="col-md-3 text-end">
        <button type="submit" class="btn btn-primary w-100">
          <i class="fas fa-search"></i> Filter
        </button>
      </div>
    </form>
    <!-- end search + filter form -->

    <div class="mb-3 text-end">
      <a href="addstaff.jsp" class="btn btn-success">
        <i class="fas fa-user-plus"></i> Add New Staff
      </a>
    </div>

    <table class="table table-striped">
      <thead>
        <tr>
          <th>User ID</th><th>Name</th><th>Email</th><th>Phone</th>
          <th>Card ID</th><th>Position</th><th>Status</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="s" items="${staffList}">
          <tr>
            <td>${s.userId}</td>
            <td>${s.firstName} ${s.lastName}</td>
            <td>${s.email}</td>
            <td>${s.phone}</td>
            <td>${s.staffCardId}</td>
            <td>${s.position}</td>
            <td>
              <span class="badge ${s.deactivated ? 'bg-secondary' : 'bg-success'}">
                ${s.deactivated ? 'Inactive' : 'Active'}
              </span>
            </td>
            <td class="text-nowrap">
              <c:choose>
                <c:when test="${s.deactivated}">
                  <a href="ReactivateStaffServlet?userId=${s.userId}"
                     class="btn btn-sm btn-outline-success">Reactivate</a>
                </c:when>
                <c:otherwise>
                  <a href="DeactivateStaffServlet?userId=${s.userId}"
                     class="btn btn-sm btn-warning">Deactivate</a>
                </c:otherwise>
              </c:choose>
              <a href="LoadEditStaffServlet?userId=${s.userId}"
                 class="btn btn-sm btn-info">Edit</a>
              <a href="DeleteStaffServlet?id=${s.userId}"
                 class="btn btn-sm btn-danger"
                 onclick="return confirm('Delete ${s.firstName} ${s.lastName}?')">
                Delete
              </a>
              <a href="StaffAccessLogServlet?userId=${s.userId}"
                 class="btn btn-sm btn-secondary">Logs</a>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty staffList}">
          <tr>
            <td colspan="8" class="text-center">No staff members found.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</body>
</html>
