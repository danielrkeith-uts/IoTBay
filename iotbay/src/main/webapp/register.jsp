<%@ page import="model.User"%>
<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        String error = (String) session.getAttribute("registerError");
        session.removeAttribute("registerError");
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <script src="register.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp">Products</a>
                <a href="shipping.jsp">Shipping</a>
                <a href="login.jsp">Login</a>
            </navbar>
        </div>
        <div class="content">
            <h2>Register</h2>
            <form action="RegisterServlet" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="text" name="email" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" />
                    <p class="fst-italic">Must be at least 8 characters long, and include an lowercase and uppercase letter, a number, and a special character</p>
                </div>
                <div class="mb-3">
                    <label for="firstName" class="form-label">First Name (optional)</label>
                    <input type="text" name="firstName" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Last Name (optional)</label>
                    <input type="text" name="lastName" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone Number (optional)</label>
                    <input type="text" name="phone" class="form-control" />
                </div>
                <div class="mb-3">
                    <input id="staff-checkbox" type="checkbox" name="isStaff" class="form-check-input" onclick="handleStaffCheckbox()"/>
                    <label for="is-staff" class="form-check-label">Register as staff</label>
                </div>
                <div id="staff-section" style="display: none">
                    <div class="mb-3">
                        <label for="staffCardId" class="form-label">Staff Card ID</label>
                        <input type="text" name="staffCardId" class="form-control" />
                    </div>
                    <div class="mb-3">
                        <label for="adminPassword" class="form-label">Admin password</label>
                        <input type="password" name="adminPassword" class="form-control" />
                    </div>
                </div>
                <p class="error"><%= (error == null ? "" : error) %></p>
                <input type="submit" class="btn-green">
            </form>
        </div>
    </body>
</html>
