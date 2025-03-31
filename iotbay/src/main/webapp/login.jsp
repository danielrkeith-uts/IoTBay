<html>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="login.css" />
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
                <a href="register.jsp">Register</a>
                <a class="active">Login</a> 
            </navbar>
        </div>
        <div class="content">
            <h2>Login</h2>
            <form>
                <label for="username">Username</label>
                <input name="username" placeholder="Enter your username" />
                <label for="password">Password</label>
                <input name="password" type="password" placeholder="Enter your password" />
                <input type="submit" />
            </form>
        </div>
    </body>
</html>
