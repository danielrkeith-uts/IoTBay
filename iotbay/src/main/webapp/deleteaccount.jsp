<html>
    <jsp:include page="/ConnServlet" flush="true"/>
    <%
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
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
            <h2>Delete account</h2>
            <form action="DeleteAccountServlet" method="post">
                <p>Are you sure you want to delete your account?</p>
                <p class="fst-italic">(This action cannot be undone)</p>
                <input type="submit" class="btn btn-danger" value="Delete account" />
            </form>
        </div>
    </body>
</html>