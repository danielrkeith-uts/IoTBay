<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Payment" %>
<%@ page import="model.Card" %>
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

    List<Payment> savedPayments = paymentManager != null ? paymentManager.getAllPaymentsForUser(user.getUserId()) : null;
%>

<html>
<head>
    <title> Payment Form </title>
    <link rel="stylesheet" href="main.css" />
    <link rel="stylesheet" href="raisedbox.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>

    <style>
        .page-container {
            min-height: 80vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        #savedPaymentsPanel {
            max-width: 400px;
            margin-left: 20px;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 12px 30px rgba(0,0,0,0.1);
            padding: 20px;
            display: none;
        }

        #savedPaymentsPanel h3 {
            margin-bottom: 20px;
            text-align: center;
        }

        .payment-method {
            border-bottom: 1px solid #ddd;
            padding: 10px 0;
        }

        .payment-method:last-child {
            border-bottom: none;
        }

        .btn-small {
            font-size: 0.8rem;
            padding: 2px 8px;
        }

        .btn-space {
            margin-left: 5px;
        }
    </style>

    <script>
        function validatePaymentForm() {
            const cardNumber = document.getElementById("cardNumber").value.trim();
            const cvc = document.getElementById("cvc").value.trim();
            const amount = parseFloat(document.getElementById("amount").value);
            const orderId = document.getElementById("orderId").value.trim();

            if (isNaN(amount) || amount <= 0) {
                alert("Please enter a valid positive amount.");
                return false;
            }

            if (!/^\d{13,16}$/.test(cardNumber)) {
                alert("Card Number must be 13 to 16 digits.");
                return false;
            }

            if (!/^\d{3,4}$/.test(cvc)) {
                alert("CVC must be 3 or 4 digits.");
                return false;
            }

            if (!/^\d+$/.test(orderId) || orderId.length === 0) {
                alert("Please enter a valid Order ID.");
                return false;
            }

            return true;
        }

        function toggleSavedPayments() {
            const panel = document.getElementById('savedPaymentsPanel');
            if (panel.style.display === 'none' || panel.style.display === '') {
                panel.style.display = 'block';
            } else {
                panel.style.display = 'none';
            }
        }
    </script>
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

<div class="page-container container">
    <div class="row w-100 justify-content-center">
        <div class="col-md-6">
            <div class="raisedbox">
                <h2>Make a Payment</h2>

                <form action="PaymentServlet" method="post" onsubmit="return validatePaymentForm()">
                    <div class="mb-3">
                        <label for="amount" class="form-label">Amount:</label>
                        <input type="number" step="0.01" min="0" name="amount" id="amount" class="form-control" 
                               required
                               value= '<%= request.getAttribute("cartTotal") != null ? request.getAttribute("cartTotal") : "" %>' />
                    </div>

                    <div class="mb-3">
                        <label for="cardName" class="form-label">Card Name:</label>
                        <input type="text" name="cardName" id="cardName" class="form-control" required />
                    </div>

                    <div class="mb-3">
                        <label for="cardNumber" class="form-label">Card Number:</label>
                        <input type="text" name="cardNumber" id="cardNumber" maxlength="16" class="form-control" 
                               pattern="\d{13,16}" title="Please enter 13 to 16 digits" required />
                    </div>

                    <div class="mb-3">
                        <label for="cardExpiry" class="form-label">Card Expiry (YYYY-MM):</label>
                        <input type="month" name="cardExpiry" id="cardExpiry" class="form-control" required />
                    </div>

                    <div class="mb-3">
                        <label for="cvc" class="form-label">CVC:</label>
                        <input type="text" name="cvc" id="cvc" maxlength="4" class="form-control" 
                               pattern="\d{3,4}" title="3 or 4 digit CVC" required />
                    </div>

                    <div class="mb-3">
                        <label for="orderId" class="form-label">Order ID:</label>
                        <input type="number" name="orderId" id="orderId" class="form-control" required />
                    </div>

                    <button type="submit" class="btn btn-primary me-2">Pay</button>
                    <button type="button" class="btn btn-secondary" onclick="toggleSavedPayments()">Saved Payment Methods</button>
                </form>
            </div>
        </div>

        <div class="col-md-4" id="savedPaymentsPanel">
            <h3>Saved Payment Methods</h3>
            <%
                if (savedPayments == null || savedPayments.isEmpty()) {
            %>
                <p>No saved payment methods. Add one by making a payment.</p>
            <%
                } else {
                    for (Payment payment : savedPayments) {
                        Card card = payment.getCard();
            %>
                <div class="payment-method d-flex justify-content-between align-items-center">
                    <div>
                        <strong><%= card.getName() %></strong><br/>
                        **** **** **** <%= card.getNumber().substring(card.getNumber().length() - 4) %><br/>
                        Expiry: <%= card.getExpiry().toString() %>
                    </div>
                    <div>
                        <a href="PaymentServlet?action=update&id=<%= payment.getPaymentId() %>" 
                           class="btn btn-sm btn-outline-primary btn-small btn-space">Update</a>
                        <a href="PaymentServlet?action=delete&id=<%= payment.getPaymentId() %>" 
                           onclick="return confirm('Delete this saved payment method?');"
                           class="btn btn-sm btn-outline-danger btn-small btn-space">Delete</a>
                    </div>
                </div>
            <%
                    }
                }
            %>

            <hr />
            <a href="#paymentForm" onclick="toggleSavedPayments()" class="btn btn-success w-100">Add a Payment Method</a>
        </div>
    </div>
</div>
</body>
</html>