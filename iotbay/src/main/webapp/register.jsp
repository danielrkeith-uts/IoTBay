<html>
    <link rel="stylesheet" href="index.css" />
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
            <form action="index.jsp" methods="post">
                <label>First Name: </label><br>
                <input type="text" name="fname"><br>
                <label>Last Name: </label><br>
                <input type="text" name="lname"><br>
                <label>Email: </label><br>
                <input type="text" name="email"><br>
                <label>Phone Number: </label><br>
                <input type="text" name="number"><br>
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
