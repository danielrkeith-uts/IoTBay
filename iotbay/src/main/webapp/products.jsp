<%@ page import="model.User"%>
<html>
    <%
        User user = (User)session.getAttribute("user");
    %>
    <head>
        <link rel="stylesheet" href="main.css" />
        <link rel="stylesheet" href="products.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp">Home</a>
                <a href="products.jsp" class="active">Products</a>
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
                <a href="cart.jsp">
                    <i class="bi bi-cart"></i>
                </a>
            </navbar>
        </div>
        <div class="content">
            <h2>Products</h2>
             <div class="product-grid">
                <div class="product-card">
                    <img src="https://www.iot-store.com.au/cdn/shop/products/home8-smart-home-valuable-tracking-sensor-home8-161035747347.png?crop=center&height=533&v=1550466641&width=533" alt="Tracking Sensor - Home 8">
                    <h3>Tracking Sensor - Home 8</h3>
                    <p>$40.00</p>
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="productName" value="Tracking Sensor - Home 8" />
                        <input type="hidden" name="price" value="40.00" />
                        <input type="hidden" name="quantity" value="1" />
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </form>
                </div>
                <div class="product-card">
                    <img src="https://www.iot-store.com.au/cdn/shop/products/bivocom-gateway-default-single-sim-tg462-industrial-cellular-4g-touch-screen-iot-edge-gateway-13672912027735.jpg?crop=center&height=720&v=1581765519&width=720" alt="TG462 Industrial Cellular 4G LTE Touch Screen IOT Edge Gateway">
                    <h3>TG462 Industrial Cellular 4G LTE Touch Screen IOT Edge Gateway</h3>
                    <p>$700.00</p>
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="productName" value="TG462 Industrial Cellular 4G LTE Touch Screen IOT Edge Gateway" />
                        <input type="hidden" name="price" value="700.00" />
                        <input type="hidden" name="quantity" value="1" />
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </form>
                </div>
                <div class="product-card">
                    <img src="https://www.firgelliauto.com.au/cdn/shop/products/FA-35-S_FA-150-S_FA-240-S_Light_Duty-min_540x_d36f03bb-c3ed-410e-8f6e-adc7b3f40291_300x.jpg?v=1677875868" alt="Classic Rod Linear Actuator">
                    <h3>Classic Rod Linear Actuator</h3>
                    <p>$150.00</p>
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="productName" value="Classic Rod Linear Actuator" />
                        <input type="hidden" name="price" value="150.00" />
                        <input type="hidden" name="quantity" value="1" />
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </form>
                </div>
                <div class="product-card">
                    <img src="https://www.iot-store.com.au/cdn/shop/products/industrial-shields-open-plc-plc-arduino-ardbox-20-i-os-analog-hf-modbus-open-industrial-plc-28536079123.jpg?crop=center&height=1066&v=1550410414&width=1066" alt="PLC Arduino ARDBOX 20 I/Os Analog - Open Industrial PLC">
                    <h3>PLC Arduino ARDBOX 20 I/Os Analog - Open Industrial PLC</h3>
                    <p>$360.00</p>
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="productName" value="PLC Arduino ARDBOX 20 I/Os Analog - Open Industrial PLC" />
                        <input type="hidden" name="price" value="360.00" />
                        <input type="hidden" name="quantity" value="1" />
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </form>
                </div>
                 <div class="product-card">
                    <img src="https://m.media-amazon.com/images/I/51Y9gLPnLCL.__AC_SX300_SY300_QL70_ML2_.jpg" alt="SHELFY Smart Refrigerator Device">
                    <h3>SHELFY Smart Refrigerator Device</h3>
                    <p>$240.00</p>
                    <form action="CartServlet" method="post">
                        <input type="hidden" name="productName" value="SHELFY Smart Refrigerator Device" />
                        <input type="hidden" name="price" value="240.00" />
                        <input type="hidden" name="quantity" value="1" />
                        <button type="submit" class="btn btn-primary">Add to Cart</button>
                    </form>
                </div>
            </div>
        </div>
        </div>
    </body>
</html>