<%@ page import="model.Order" %>
<%@ page import="model.ProductListEntry" %>
<%@ page import="java.util.List" %>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");

    if (orders == null || orders.isEmpty()) {
%>
        <h2>No orders found.</h2>
<%
    } else {
        for (Order order : orders) {
            String orderDate = order.getDatePlaced().toString(); // Or format the date if needed
%>
        <div class="order">
            <h3>Order Date: <%= orderDate %></h3>
            <h4>Order Details:</h4>
            <ul>
                <%
                    List<ProductListEntry> productList = order.getProductList();
                    for (ProductListEntry entry : productList) {
                %>
                    <li><%= entry.getProduct().getName() %> - Quantity: <%= entry.getQuantity() %> - Total: <%= entry.totalCost() %></li>
                <%
                    }
                %>
            </ul>
            <p><strong>Payment Method:</strong> <%= order.getPayment().getPaymentMethod() %></p>
            <p><strong>Delivery:</strong> <%= order.getDelivery().getDeliveryAddress() %></p>
        </div>
        <hr>
<%
        }
    }
%>
