<html>
    <jsp:include page="/LogoutServlet" flush="true"/>
    <head>
        <link rel="stylesheet" href="css/main.css" />
        <link rel="stylesheet" href="css/raisedbox.css" />
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
            <div class="raisedbox">
                <div class="check-icon text-center">
                    <i class="fas fa-check-circle"></i>
                </div>
                <h2 class="animated-title">You have successfully logged out</h2>
                <p>Click <a href="index.jsp">here</a> to proceed to the main page.</p>
            </div>
        </div>
    </body>
</html>