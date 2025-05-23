<%@ page import="java.util.List" %>
<%@ page import="model.Payment" %>
<%@ page import="model.Card" %>
<%@ page import="model.Enums.PaymentStatus" %>
<%@ page import="model.User" %>
<%@ page import="model.dao.PaymentDBManager" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    PaymentDBManager paymentManager = (PaymentDBManager) application.getAttribute("paymentDBManager");
    if (paymentManager == null) {
        out.println("<p>Error: Payment system is currently unavailable. Please try again later.</p>");
        return;
    }

    List<Payment> payments = (List<Payment>) request.getAttribute("payments");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>

<html>
<head>
    <title>Payment History</title>
    <link rel="stylesheet" href="main.css" />
    <link rel="stylesheet" href="raisedbox.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
</head>
<body>
    <div class="banner">
        <h1>Internet of Things Store</h1>
        <nav>
            <a href="index.jsp">Home</a>
            <a href="products.jsp">Products</a>
            <a href="login.jsp">Login</a>
        </nav>
    </div>

    <div class="raisedbox">
        <div class="text-center mb-4">
            <i class="fas fa-wallet fa-2x text-success"></i>
            <h2>Your Payment History</h2>
        </div>

        <% String message = (String) session.getAttribute("message"); %>
        <% if (message != null) { %>
            <div class="alert alert-success"><%= message %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        <% String error = (String) session.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
            <% session.removeAttribute("error"); %>
        <% } %>

        <form method="get" action="PaymentServlet" class="row g-3 align-items-end mb-4">
            <div class="col-md-3">
                <label for="status" class="form-label">Payment Status:</label>
                <select name="status" id="status" class="form-select">
                    <option value="" <%= (request.getParameter("status") == null || request.getParameter("status").isEmpty()) ? "selected" : "" %>>-- All --</option>
                    <option value="PENDING" <%= "PENDING".equals(request.getParameter("status")) ? "selected" : "" %>>Pending</option>
                    <option value="ACCEPTED" <%= "ACCEPTED".equals(request.getParameter("status")) ? "selected" : "" %>>Accepted</option>
                    <option value="FAILED" <%= "FAILED".equals(request.getParameter("status")) ? "selected" : "" %>>Failed</option>
                </select>
            </div>
            <div class="col-md-3">
                <label for="fromDate" class="form-label">From Date:</label>
                <input type="date" name="fromDate" id="fromDate" class="form-control" value="<%= request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "" %>">
            </div>
            <div class="col-md-3">
                <label for="toDate" class="form-label">To Date:</label>
                <input type="date" name="toDate" id="toDate" class="form-control" value="<%= request.getParameter("toDate") != null ? request.getParameter("toDate") : "" %>">
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-success w-100"><i class="fas fa-search"></i> Search</button>
            </div>
        </form>

        <table class="table table-striped table-bordered text-center">
            <thead class="table-dark">
                <tr>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Card Name</th>
                    <th>Card Number</th>
                    <th>Card Expiry</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (payments == null || payments.isEmpty()) {
                %>
                    <tr><td colspan="7">No payments found.</td></tr>
                <%
                    } else {
                        for (Payment payment : payments) {
                            Card card = payment.getCard();
                %>
                    <tr>
                        <td><%= payment.getAmount() %></td>
                        <td><span class="badge bg-<%= payment.getPaymentStatus() == PaymentStatus.ACCEPTED ? "success" : payment.getPaymentStatus() == PaymentStatus.PENDING ? "warning" : "danger" %>">
                            <%= payment.getPaymentStatus() %></span>
                        </td>
                        <td><%= sdf.format(payment.getDate()) %></td>
                        <td><%= card.getName() %></td>
                        <td><%= card != null ? card.getName() : "N/A" %></td>
                        <td><%
                        String cardNumber = card != null ? card.getNumber() : null;
                        if (cardNumber != null && cardNumber.length() >= 4) {
                            out.print("**** **** **** " + cardNumber.substring(cardNumber.length() - 4));
                        } else if (cardNumber != null) {
                            out.print(cardNumber);
                        } else {
                            out.print("N/A");
                        }
                        %></td>
                        <td><%= card != null ? card.getExpiry().toString() : "N/A" %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/PaymentServlet?action=delete&id=<%= payment.getPaymentId() %>" 
                            class="btn btn-sm btn-danger"
                            onclick="return confirm('Are you sure you want to delete this payment?');">
                            Delete
                            </a>
                        </td>
                    </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
