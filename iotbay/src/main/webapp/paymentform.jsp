<%@ page import="java.util.List" %>
<%@ page import="model.Payment" %>
<%@ page session="true" %>
<%
    Integer userId = (Integer) session.getAttribute("userId");

    List<Payment> payments = null;
    if (userId != null) {
        payments = (List<Payment>) request.getAttribute("payments");
    }

    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Payment Form</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        .card { border: 1px solid #ccc; padding: 15px; margin-bottom: 10px; border-radius: 5px; }
        .card:hover { background-color: #f9f9f9; }
        .form-section { margin-top: 20px; }
        .hidden { display: none; }
        button { margin-right: 10px; }
    </style>
    <script>
        function toggleForm(id) {
            var form = document.getElementById(id);
            form.classList.toggle('hidden');
        }

        function confirmDelete(id) {
        if (confirm("Are you sure you want to delete this payment method?")) {
            window.location.href = "PaymentServlet?action=deleteCard&cardId=" + id;
            }
        }


        function selectCard(cardId, number) {
            document.getElementById("selectedCard").innerText = "Card ending in " + number.slice(-4);
            document.getElementById("selectedCardId").value = cardId;
        }
    </script>
</head>
<body>

    <h2>Make a Payment</h2>

    <% if (message != null) { %>
        <p style="color:green;"><%= message %></p>
    <% } %>

    <div class="form-section">
        <form action="PaymentServlet" method="post">
            <input type="hidden" name="action" value="pay">
            <input type="hidden" name="cardId" id="selectedCardId" />
            <p>Selected Card: <strong id="selectedCard">None</strong></p>
            <label for="amount">Amount:</label>
            <input type="number" name="amount" step="0.01" required />
            <button type="submit">Pay</button>
        </form>
    </div>

    <h3>Saved Payment Methods</h3>

    <% if (payments != null && !payments.isEmpty()) {
        for (Payment p : payments) {
    %>
        <div class="payment-entry">
        <p><strong>Payment ID:</strong> <%= p.getPaymentId() %></p>
        <p><strong>Amount:</strong> $<%= String.format("%.2f", p.getAmount()) %></p>
        <p><strong>Date:</strong> <%= p.getDate() != null ? p.getDate() : "N/A" %></p>
        <p><strong>Status:</strong> <%= p.getPaymentStatus() %></p>

        <p><strong>Card Number:</strong> **** **** **** 
            <%= p.getCard().getNumber().length() >= 4 ? 
                p.getCard().getNumber().substring(p.getCard().getNumber().length() - 4) : "****" %>
        </p>
        <p><strong>Cardholder Name:</strong> <%= p.getCard().getName() %></p>
        <p><strong>Expiry:</strong> 
            <%= String.format("%02d/%04d", p.getCard().getExpiry().getMonthValue(), p.getCard().getExpiry().getYear()) %>
        </p>

        <button onclick="selectCard('<%= p.getPaymentId() %>', '<%= p.getCard().getNumber() %>')">Use This</button>
        <button onclick="toggleForm('updateForm<%= p.getPaymentId() %>')">Update</button>
        <button onclick="confirmDelete(<%= p.getPaymentId() %>)">Delete</button>

        <div id="updateForm<%= p.getPaymentId() %>" class="hidden">
            <form action="PaymentServlet" method="post">
                <input type="hidden" name="action" value="editCard" />
                <input type="hidden" name="cardId" value="<%= p.getCard().getCardId() %>" />
                <input type="hidden" name="paymentId" value="<%= p.getPaymentId() %>" />

                <p>
                    <label for="cardNumber<%= p.getPaymentId() %>">Card Number:</label><br/>
                    <input type="text" id="cardNumber<%= p.getPaymentId() %>" name="cardNumber" 
                        value="<%= p.getCard().getNumber() %>" required />
                </p>
                <p>
                    <label for="cardName<%= p.getPaymentId() %>">Cardholder Name:</label><br/>
                    <input type="text" id="cardName<%= p.getPaymentId() %>" name="cardName" 
                        value="<%= p.getCard().getName() %>" required />
                </p>
                <p>
                    <label for="cardCVC<%= p.getPaymentId() %>">CVC:</label><br/>
                    <input type="text" id="cardCVC<%= p.getPaymentId() %>" name="cardCVC" 
                        value="<%= p.getCard().getCvc() %>" required />
                </p>
                <p>
                    <label for="cardExpiry<%= p.getPaymentId() %>">Expiry (YYYY-MM):</label><br/>
                    <input type="text" id="cardExpiry<%= p.getPaymentId() %>" name="cardExpiry" 
                        value="<%= String.format("%04d-%02d", p.getCard().getExpiry().getYear(), p.getCard().getExpiry().getMonthValue()) %>" 
                        required pattern="\\d{4}-\\d{2}" 
                        title="Format: YYYY-MM" />
                </p>

                <p>
                    <button type="submit">Save Changes</button>
                    <button type="button" onclick="toggleForm('updateForm<%= p.getPaymentId() %>')">Cancel</button>
                </p>
            </form>
        </div>
    </div>
    <% } } else { %>
        <p>No saved payment methods.</p>
    <% } %>

    <h3>Add New Payment Method</h3>
    <div class="form-section">
        <form action="PaymentServlet" method="post">
        <input type="hidden" name="action" value="add" />
        <p><input type="text" name="cardNumber" placeholder="Card Number" required /></p>
        <p><input type="text" name="cardName" placeholder="Cardholder Name" required /></p>
        <p><input type="text" name="cardCVC" placeholder="CVC" required /></p>
        <p><input type="text" name="cardExpiry" placeholder="YYYY-MM" pattern="\d{4}-\d{2}" title="Format: YYYY-MM" required /></p>
        <button type="submit">Save New Payment Method</button>
        </form>
    </div>

</body>
</html>