<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.util.*,model.User,model.Staff,model.ApplicationAccessLog" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    User user = (User) session.getAttribute("user");
    boolean isAdmin = (user instanceof Staff) && ((Staff)user).isAdmin();
    if (!isAdmin) {
        response.sendError(403,"Forbidden");
        return;
    }
    Integer staffId = (Integer)request.getAttribute("staffId");
    List<ApplicationAccessLog> logs = (List<ApplicationAccessLog>)request.getAttribute("logs");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Staff Access Logs</title>
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
      <a href="StaffListServlet">Staff</a>
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
    <h2>Access Logs for Staff ID ${staffId}</h2>
    <a href="StaffListServlet" class="btn btn-secondary mb-3">← Back to Staff List</a>

    <table class="table table-bordered">
      <thead>
        <tr><th>Log ID</th><th>Action</th><th>Date &amp; Time</th></tr>
      </thead>
      <tbody>
        <c:forEach var="log" items="${logs}">
          <tr>
            <td>${log.accessLogId}</td>
            <td>${log.applicationAction}</td>
            <td>${log.dateTime}</td>
          </tr>
        </c:forEach>
        <c:if test="${empty logs}">
          <tr>
            <td colspan="3" class="text-center">No logs recorded.</td>
          </tr>
        </c:if>
      </tbody>
    </table>
  </div>
</body>
</html>


