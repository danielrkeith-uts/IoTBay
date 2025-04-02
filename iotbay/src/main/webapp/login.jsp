<html>
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
                <a href="login.jsp" class="active">Login</a>
            </navbar>
        </div>
        <div class="content">
            <h2>Login</h2>
            <form action="welcome.jsp" methods="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input name="email" class="form-control" placeholder="Enter your email" />
                </div>
                <div class="mb-3">
                    <label for="password" c-ass="form-label">Password</label>
                    <input name="password" type="password" class="form-control" placeholder="Enter your password" />
                </div>
                <input type="checkbox" name="tos" checked hidden>
                <input type="submit" class="btn-green" />
            </form>
            <p>Don't have an account? Register <a href="register.jsp">here</a></p>
        </div>
    </body>
</html>
