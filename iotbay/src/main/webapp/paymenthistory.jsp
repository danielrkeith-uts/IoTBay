<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Staff, model.Payment, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Payment> payments = (List<Payment>) request.getAttribute("payments");
%>
<html>
<head>
  <title>Payment History</title>
  <link rel="stylesheet" href="main.css" />
  <link rel="stylesheet" href="raisedbox.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
  <style>
    body {
      background-color: #f5f5f5;
    }
    .content {
      padding: 20px;
      max-width: 800px;
      margin: 0 auto;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }
    th, td {
      border: 1px solid #ddd;
      padding: 8px;
      text-align: left;
    }
    th {
      background-color: #f5f5f5;
    }
    .btn-green {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      margin-top: 10px;
    }
    input[type="text"], input[type="date"] {
      width: 100%;
      padding: 8px;
      margin: 5px 0;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    label {
      font-weight: bold;
      display: block;
      margin-top: 10px;
    }
    .search-form {
      margin-bottom: 20px;
    }
    .banner {
      background-color: #333;
      padding: 20px;
      margin-bottom: 30px;
    }
    .banner h1 {
      color: white;
      text-align: center;
      margin-bottom: 20px;
    }
    .navbar {
      background-color: transparent;
    }
    .navbar .nav-link {
      color: white !important;
      padding: 8px 16px;
    }
    .navbar .nav-link:hover {
      background-color: rgba(255, 255, 255, 0.1);
      border-radius: 4px;
    }
    .dropdown-menu {
      margin-top: 8px;
    }
  </style>
</head>
<body>
  <div class="banner">
    <h1>Internet of Things Store</h1>
    <nav class="navbar navbar-expand-lg">
      <div class="container-fluid">
        <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" href="index.jsp">Home</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="products.jsp">Products</a>
            </li>
            <% if (user == null) { %>
              <li class="nav-item">
                <a class="nav-link" href="login.jsp">Login</a>
              </li>
            <% } else { %>
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                <ul class="dropdown-menu">
                  <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                  <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                  <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                  <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                  <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                </ul>
              </li>
            <% } %>
            <li class="nav-item">
              <a class="nav-link" href="cart.jsp">
                <i class="bi bi-cart"></i>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>

  <div class="content">
    <div class="raisedbox">
      <h2>Your Payment History</h2>
      <div class="search-form">
        <form action="PaymentServlet" method="get" class="row g-3">
          <input type="hidden" name="action" value="viewHistory" />
          <div class="col-md-4">
            <label>Payment ID:</label>
            <input type="text" name="paymentId" class="form-control" placeholder="Enter payment ID" />
          </div>
          <div class="col-md-4">
            <label>Date From:</label>
            <input type="date" name="dateFrom" class="form-control" />
          </div>
          <div class="col-md-4">
            <label>Date To:</label>
            <input type="date" name="dateTo" class="form-control" />
          </div>
          <div class="col-12">
            <button type="submit" class="btn-green">Search</button>
          </div>
        </form>
      </div>

      <% if (payments != null && !payments.isEmpty()) { %>
        <div class="table-responsive">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>Amount</th>
                <th>Card Number</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              <% for (Payment payment : payments) { %>
                <tr>
                  <td><%= payment.getPaymentId() %></td>
                  <td>$<%= String.format("%.2f", payment.getAmount()) %></td>
                  <td>
                    <% if (payment.getCard() != null) { %>
                      <%= payment.getCard().getNumber().replaceAll("\\d(?=\\d{4})", "*") %>
                    <% } else { %>
                      Guest Payment
                    <% } %>
                  </td>
                  <td><%= payment.getPaymentStatus() %></td>
                </tr>
              <% } %>
            </tbody>
          </table>
        </div>
      <% } else { %>
        <div class="alert alert-info mt-3">
          No payment history found.
        </div>
      <% } %>
    </div>
  </div>

  <jsp:include page="footer.jsp" />
</body>
</html>
