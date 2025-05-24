<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User, model.Staff, model.Cart, model.Card, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    Cart cart = (Cart) session.getAttribute("cart");
    Double amount = cart != null ? cart.totalCost() : null;
    List<Card> paymentMethods = (List<Card>) request.getAttribute("paymentMethods");
    System.out.println("User: " + (user != null ? user.getUserId() : "null"));
    System.out.println("Payment Methods: " + (paymentMethods != null ? paymentMethods.size() : "null"));
%>
<html>
<head>
  <title>Payment Form</title>
  <link rel="stylesheet" href="main.css" />
  <link rel="stylesheet" href="raisedbox.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
  <style>
    body {
      background-color: #f5f5f5;
    }
    .content {
      padding: 20px;
      max-width: 800px;
      margin: 0 auto;
      position: relative;
    }
    .method-item {
      border: 1px solid #ddd;
      padding: 15px;
      margin: 10px 0;
      border-radius: 4px;
      background: white;
      position: relative;
    }
    .method-item .actions {
      position: absolute;
      right: 15px;
      top: 15px;
    }
    .btn-green {
      background-color: #4CAF50;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
      display: inline-block;
      margin: 5px;
    }
    .btn-green:hover {
      background-color: #45a049;
      color: white;
      text-decoration: none;
    }
    input[type="text"], input[type="month"] {
      width: 100%;
      padding: 8px;
      margin: 5px 0;
      border: 1px solid #ddd;
      border-radius: 4px;
    }
    label {
      font-weight: bold;
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
    .saved-methods-panel {
      position: fixed;
      top: 0;
      right: -400px;
      width: 400px;
      height: 100vh;
      background: white;
      box-shadow: -2px 0 5px rgba(0,0,0,0.1);
      padding: 20px;
      transition: right 0.3s ease;
      overflow-y: auto;
      z-index: 1050;
    }
    .saved-methods-panel.active {
      right: 0;
      display: block !important;
    }
    .panel-overlay {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: rgba(0,0,0,0.5);
      display: none;
      z-index: 1040;
    }
    .panel-overlay.active {
      display: block !important;
    }
    .navigation-buttons {
      margin: 20px 0;
      text-align: right;
    }
    .guest-notice {
      background-color: #f8f9fa;
      padding: 15px;
      margin-bottom: 20px;
      border-radius: 4px;
      border: 1px solid #dee2e6;
    }
    .guest-notice p {
      margin-bottom: 10px;
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
    <% if (user == null) { %>
      <div class="guest-notice">
        <p>You are checking out as a guest. <a href="login.jsp">Login</a> to:</p>
        <ul>
          <li>Save your payment methods for future use</li>
          <li>Track your order history</li>
          <li>Get faster checkout next time</li>
        </ul>
      </div>
    <% } else { %>
      <div class="navigation-buttons">
        <a href="PaymentServlet?action=viewHistory" class="btn btn-primary">View Payment History</a>
        <button type="button" class="btn btn-secondary" onclick="toggleSavedMethods()">View Saved Methods</button>
      </div>
    <% } %>

    <div class="raisedbox">
      <h2>Complete Your Payment</h2>
      <% if (amount == null) { %>
        <div class="alert alert-warning">
          No payment amount specified. Please return to your cart.
          <a href="cart.jsp" class="btn btn-primary">Return to Cart</a>
        </div>
      <% } else { %>
        <form action="PaymentServlet" method="post" id="paymentForm">
          <input type="hidden" name="action" value="createPayment" />
          <input type="hidden" name="amount" value="<%= amount %>" />
          
          <p>Amount to pay: <strong>$<%= String.format("%.2f", amount) %></strong></p>

          <div class="payment-form">
            <h3>Payment Details</h3>
            <p><label>Name on Card:<br/>
              <input type="text" name="name" id="cardName" required />
            </label></p>
            <p><label>Card Number:<br/>
              <input type="text" name="cardNumber" id="cardNumber" required pattern="[0-9]{16}" maxlength="16" />
            </label></p>
            <p><label>Expiry Date:<br/>
              <input type="month" name="expiry" id="cardExpiry" required />
            </label></p>
            <p><label>CVV:<br/>
              <input type="text" name="cvv" id="cardCvv" required pattern="[0-9]{3,4}" maxlength="4" />
            </label></p>
            <% if (user != null) { %>
              <p>
                <label>
                  <input type="checkbox" name="saveMethod" />
                  Save this method for future use
                </label>
              </p>
            <% } %>
          </div>

          <button type="submit" class="btn-green">Pay Now</button>
        </form>
      <% } %>
    </div>
  </div>

  <% if (user != null) { %>
    <!-- Saved Methods Panel -->
    <div class="panel-overlay" onclick="toggleSavedMethods()"></div>
    <div class="saved-methods-panel">
      <h3>Saved Payment Methods</h3>
      <div id="savedMethodsList">
        <% if (paymentMethods != null && !paymentMethods.isEmpty()) { %>
          <% for (Card method : paymentMethods) { %>
            <div class="method-item">
              <div class="card-info">
                <h4>**** **** **** <%= method.getNumber().substring(method.getNumber().length() - 4) %></h4>
                <p><%= method.getName() %></p>
                <p>Expires: <%= method.getExpiry().toString() %></p>
              </div>
              <div class="actions">
                <button onclick="useMethod('<%= method.getNumber() %>', '<%= method.getName() %>', '<%= method.getExpiry() %>')" class="btn btn-primary btn-sm">Use</button>
                <button onclick="editMethod(<%= method.getCardId() %>)" class="btn btn-secondary btn-sm">Edit</button>
                <button onclick="deleteMethod(<%= method.getCardId() %>)" class="btn btn-danger btn-sm">Delete</button>
              </div>
            </div>
          <% } %>
        <% } else { %>
          <p>No saved payment methods.</p>
        <% } %>
      </div>
    </div>
  <% } %>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    document.addEventListener('DOMContentLoaded', function() {
      const panel = document.querySelector('.saved-methods-panel');
      const overlay = document.querySelector('.panel-overlay');
      console.log('Saved methods panel exists:', panel !== null);
      console.log('Panel overlay exists:', overlay !== null);
      if (panel) {
        console.log('Panel initial display:', window.getComputedStyle(panel).display);
        console.log('Panel initial right:', window.getComputedStyle(panel).right);
      }
    });

    function toggleSavedMethods() {
      const panel = document.querySelector('.saved-methods-panel');
      const overlay = document.querySelector('.panel-overlay');
      panel.classList.toggle('active');
      overlay.classList.toggle('active');
    }

    function useMethod(number, name, expiry) {
      document.getElementById('cardNumber').value = number;
      document.getElementById('cardName').value = name;
      document.getElementById('cardExpiry').value = expiry;
      toggleSavedMethods();
    }

    function editMethod(cardId) {
      window.location.href = 'PaymentServlet?action=editMethod&cardId=' + cardId;
    }

    function deleteMethod(cardId) {
      if (confirm('Are you sure you want to delete this payment method?')) {
        window.location.href = 'PaymentServlet?action=deleteMethod&cardId=' + cardId;
      }
    }
  </script>

  <jsp:include page="footer.jsp" />
</body>
</html>