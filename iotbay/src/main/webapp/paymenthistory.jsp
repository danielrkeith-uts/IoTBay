<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment History</title>
</head>
<body>
    <h2>Your Payment History</h2>

    <table border="1">
        <tr>
            <th>Amount</th>
            <th>Card Number</th>
            <th>Payment Status</th>
            <th>Order ID</th>
            <th>Date</th>
        </tr>

        <!-- Loop through payments list -->
        <c:forEach var="payment" items="${payments}">
            <tr>
                <td>${payment.amount}</td>
                <td>${payment.card.number}</td>
                <td>${payment.paymentStatus}</td>
                <td>${payment.orderId}</td>
                <td>${payment.date}</td>
            </tr>
        </c:forEach>
    </table>

    <a href="payment_form.jsp">Make a New Payment</a>
</body>
</html>
