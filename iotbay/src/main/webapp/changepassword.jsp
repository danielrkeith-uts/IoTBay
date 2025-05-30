<%@ page import="model.User, model.Staff" %>
<html>
    <jsp:include page="/ConnServlet" flush="true"/> 
    <%
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String error = (String) session.getAttribute("changePasswordError");
        session.removeAttribute("changePasswordError");

        User user = (User) session.getAttribute("user");
        boolean isStaff = (user != null) && (user instanceof Staff);
    %>
    <head>
        <link rel="stylesheet" href="css/main.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <jsp:include page="navbar.jsp" />
        </div>
        <div class="content">
            <h2>Change password</h2>
            <form action="ChangePasswordServlet" method="post">
                <div class="mb-3">
                    <label for="old-password" class="form-label">Old Password</label>
                    <input type="password" name="old-password" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="new-password" class="form-label">New Password</label>
                    <input type="password" name="new-password" class="form-control" />
                    <p class="fst-italic">Must be at least 8 characters long, and include an lowercase and uppercase letter, a number, and a special character</p>
                </div>
                <div class="mb-3">
                    <label for="new-password-confirmation" class="form-label">Confirm New Password</label>
                    <input type="password" name="new-password-confirmation" class="form-control" />
                </div>
                <p class="error"><%= (error == null ? "" : error) %></p>
                <input type="submit" class="btn-green" value="Change Password">
            </form>
        </div>
    </body>
</html>
