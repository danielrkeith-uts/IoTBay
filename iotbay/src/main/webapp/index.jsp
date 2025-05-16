<%@ page import="model.User"%>
<html>
    <%
        User user = (User)session.getAttribute("user");
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="index.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp" class="active">Home</a>
                <a href="products.jsp">Products</a>
                <% if (user == null) { %>
                    <a href="login.jsp">Login</a>
                <% } else { %>
                    <div class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">My Account</a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="account.jsp">Account Details</a></li>
                            <li><a class="dropdown-item" href="applicationaccesslogs.jsp">Application Access Logs</a></li>
                            <li><a class="dropdown-item" href="logout.jsp">Logout</a></li>
                            <li><a class="dropdown-item text-danger" href="deleteaccount.jsp">Delete Account</a></li>
                        </ul>
                    </div>
                <% } %>
                <a href="order.jsp">
                    <i class="bi bi-cart"></i>
                </a>
            </navbar>
        </div>
        <div class="content">
            <div class="welcome-section">
                <div class="welcome-content">
                    <div class="text-container">
                        <div class="text-content">
                            <h2>Welcome to IoTBay</h2>
                            <p>We offer the best Internet of Things (IoT) products, designed to enhance your lifestyle and bring convenience to your home or business. Explore our range of smart devices and enjoy seamless integration with your everyday life.</p>
                            <a href="products.jsp">
                                <button class="btn-green">Shop Now</button>
                            </a>
                        </div>
                    </div>
                    <div class="image-container">
                        <img class="img-fluid" src="https://soracom.io/wp-content/uploads/fly-images/18345/AdobeStock_181088041-scaled-768x432-c.jpeg" alt="Smart Devices" />
                    </div>
                </div>
            </div>
            <section class="why-choose-us">
                <h2>Why Choose Us?</h2> 
                <p>We offer the best in class IoT devices and customer service.</p>
                <div class="features">
                    <div class="feature">
                        <h3>Quality Products</h3>
                                    <i class="fas fa-check-circle"></i>

                        <p>We only offer the best IoT devices from trusted brands.</p>
                    </div>
                    <div class="feature">
                        <h3>Fast Shipping</h3>
                                    <i class="fas fa-shipping-fast"></i>

                        <p>Get your orders delivered quickly and efficiently.</p>
                    </div>
                    <div class="feature">
                        <h3>24/7 Support</h3>
                                    <i class="fas fa-headset"></i>

                        <p>Our customer support team is available around the clock to assist you.</p>
                    </div>
                    <div class="feature">
                        <h3>Secure Payments</h3>
                                    <i class="fas fa-lock"></i>

                        <p>Leading class security to keep your information safe and transaction protected.</p>
                    </div>
                </div>
            </section>
        </div>
    </body>
</html>