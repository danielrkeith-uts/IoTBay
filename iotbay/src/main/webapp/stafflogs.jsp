<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,model.User,model.Staff,model.ApplicationAccessLog" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Staff Access Logs</title>
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


