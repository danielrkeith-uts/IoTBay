<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Enums.*"%>
<%@ page import="model.*"%>
<%@ page import="java.time.YearMonth" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%-- <jsp:include page="/ConfirmOrderServlet" flush="true"/> --%>
    <%
        // Retrieve session attributes
        String error = (String) session.getAttribute("cartError");
        session.removeAttribute("cartError"); 

        User user = (User) session.getAttribute("user");
        String firstName = user != null ? user.getFirstName() : (String) session.getAttribute("firstName");
        String lastName = user != null ? user.getLastName() : (String) session.getAttribute("lastName");
        String email = user != null ? user.getEmail() : (String) session.getAttribute("email");
        String phone = user != null ? user.getPhone() : (String) session.getAttribute("phone");

        // Ensure cart exists in session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Get session address 
        Address address = (Address) session.getAttribute("address");
        String streetName = address != null ? address.getStreet() : "";
        int streetNumber = address != null ? address.getStreetNumber() : -1;
        AuState state = address != null ? address.getState() : AuState.WA;
        String postCode = address != null ? address.getPostcode() : "";
        String suburb = address != null ? address.getSuburb() : "";

        Card card = (Card) session.getAttribute("card");
        String cardName = card != null ? card.getName() : "";
        String cardNumber = card != null ? card.getNumber() : "";
        YearMonth expiry = card != null ? card.getExpiry() : null;
        String cvc = card != null ? card.getCvc() : "";
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="cart.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body class="cart-page">
        <div class="banner">
            <h1>Check Out</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp">Products</a>
                <% if (user == null) { %>
                    <a href="login.jsp">Login</a>
                <% } else { %>
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                            <li><a class="dropdown-item" href="myorders.jsp">My Orders</a></li>
                            <li><a class="dropdown-item" href="shipments.jsp">My Shipments</a></li>
                            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                        </ul>
                    </div>
                <% } %>
                <a href="cart.jsp" class="bi bi-cart active"></a>
            </navbar>
            <%
                List<ProductListEntry> cartItems = cart.getProductList();
            %>
        </div>
        <h2>Products</h2>
        <ul>
            <% 
                for (ProductListEntry item : cartItems) {
            %>
                <li><%= item.getProduct().getName() %> - $<%= item.getProduct().getCost() %> x <%= item.getQuantity() %></li>
            <%
                }
            %>
        </ul>
        <p><strong>Total: $<%= cart.totalCost() %></strong></p>
        <h2>Personal Details</h2>
        <form action="ConfirmOrderServlet" method="post">
            <label for="firstName">First Name:</label><br>
            <input type="text" id="firstName" name="firstName" value="<%= firstName != null ? firstName : "" %>" required><br><br>

            <label for="lastName">Last Name:</label><br>
            <input type="text" id="lastName" name="lastName" value="<%= lastName != null ? lastName : "" %>" required><br><br>

            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" value="<%= email != null ? email : "" %>" required><br><br>

            <label for="phone">Phone Number:</label><br>
            <input type="tel" id="phone" name="phone" value="<%= phone != null ? phone : "" %>" required><br><br>
        <h2>Shipping Details</h2>
            <label for="streetNumber">Street Number:</label><br>
            <input type="text" id="streetNumber" name="streetNumber" value="<%= streetNumber != -1 ? streetNumber : ""%>" required><br><br>

            <label for="streetName">Street Name:</label><br>
            <input type="text" id="streetName" name="streetName" value="<%= streetName != null ? streetName : "" %>" required><br><br>

            <label for="suburb">Suburb:</label><br>
            <input type="text" id="suburb" name="suburb" value="<%= suburb != null ? suburb : ""%>" required><br><br>

            <label for="state">State:</label><br>
            <input type="text" id="state" name="state" value="<%= state != AuState.WA ? state : "" %>" required><br><br>

            <label for="postCode">Post Code:</label><br>
            <input type="text" id="postCode" name="postCode" value="<%= postCode != null ? postCode : "" %>" required><br><br>
        <h2>Payment Details</h2>
            <label for="cardNumber">Card Number:</label><br>
            <input type="text" id="cardNumber" name="cardNumber" value="<%= cardNumber %>" required><br><br>

            <label for="cardName">Card Name:</label><br>
            <input type="text" id="cardName" name="cardName" value="<%= cardName %>" required><br><br>

            <label for="expiry">Expiration Date:</label><br>
            <input type="text" id="expiry" name="expiry" value="<%=expiry != null ? expiry.toString() : "" %>" required><br><br>

            <label for="cvc">CVC:</label><br>
            <input type="text" id="cvc" name="cvc" value="<%= cvc %>" required><br><br>
            <button type="submit" class="btn btn-success">Confirm Order</button>
        </form>
    </body>
</html>