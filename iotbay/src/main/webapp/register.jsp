<html>
    <link rel="stylesheet" href="index.css" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp">Products</a>
                <a href="login.jsp" class="login-logout">Login</a> 
                <a href="register.jsp" class="active">Register</a>
            </navbar>
        </div>
        <div class="content">
            <h2>Register Here</h2>
            <form action="welcome.jsp" methods="post">
                <label>First Name: </label><br>
                <input type="text" name="firstName"><br>
                <label>Last Name: </label><br>
                <input type="text" name="lastName"><br>
                <label>Email: </label><br>
                <input type="text" name="email"><br>
                <label>Phone Number: </label><br>
                <input type="text" name="phone"><br>
                <label>Password: </label><br>
                <input type="password" name="password"><br>
                <label>Gender: </label><br>
                <input type="text" name="gender"><br>
                <label>Agree to TOS: </label><br>
                <input type="checkbox" name="tos"><br>
                <input type="submit" value="Submit">
            </form>
        </div>
    </body>
</html>
