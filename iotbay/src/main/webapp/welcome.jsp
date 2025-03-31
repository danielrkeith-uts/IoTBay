<%@ page import="model.User"%>
<html>
    <%!
        public boolean isNullOrEmpty(String str) {
            return str == null || str.isEmpty();
        }

        public String valueOrUnknown(String value) {
            return isNullOrEmpty(value) ? "unknown" : value;
        }
    %>
    <%
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String tos = request.getParameter("tos");
        User user = new User(firstName, lastName, email, phone, password, gender);
        session.setAttribute("user", user);
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp">Products</a>
                <a href="login.jsp">Login</a> 
            </navbar>
        </div>
        <div class="content">
            <% 
                if (tos != null && tos.equals("on")) {
            %>
                <h2>Welcome<%= (isNullOrEmpty(firstName) ? "" : ", " + firstName)  %>!</h2>
                <p>Your last name is <%= valueOrUnknown(lastName) %>.</p>
                <p>Your email is <%= valueOrUnknown(email) %>.</p>
                <p>Your phone number is <%= valueOrUnknown(phone) %>.</p>
                <p>Your password is <%= valueOrUnknown(password) %>.</p>
                <p>Your gender is <%= valueOrUnknown(gender) %>.</p>
                <p>Click <a href="index.jsp">here</a> to proceed to the main page.</p>

            <% 
                } else { 
            %>
                <p>
                Sorry, you must agree to the Terms of Service. </br>
                Click <a href="register.jsp">here</a> to go back.</br>
                </p>
            <% 
                }
            %>
    </body>
</html>