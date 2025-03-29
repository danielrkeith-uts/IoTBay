<link rel="stylesheet" href="index.css" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.min.js"></script>
    <body>
        <div class="banner">
            <h1>Internet of Things Store</h1>
            <navbar>
                <a href="index.jsp" class="active">Home</a>
                <a href="products.jsp">Products</a>
                <a href="login.jsp" class="login-logout">Login</a> 
                <a href="register.jsp">Register</a>
            </navbar>
        </div>
        <div class="content">
<div class="welcome-section">
    <div class="welcome-content">
        <div class="text-container">
            <div class="text-content">
                <h2>Welcome to IoTBay</h2>
                <p>We offer the best Internet of Things (IoT) products, designed to enhance your lifestyle and bring convenience to your home or business. Explore our range of smart devices and enjoy seamless integration with your everyday life.</p>
                <button>Shop Now</button>
            </div>
        </div>
        
        <div class="image-container">
            <div class="image-content">
                <img src="https://soracom.io/wp-content/uploads/fly-images/18345/AdobeStock_181088041-scaled-768x432-c.jpeg" alt="Smart Devices" />
            </div>
        </div>
    </div>
</div>

   <section class="why-choose-us">
        <h2>Why Choose Us?</h2>
        <p>We offer the best in class IoT devices and customer service.</p>
        <div class="features">
            <div class="feature">
                <h3>Quality Products</h3>
                <p>We only offer the best IoT devices from trusted brands.</p>
            </div>
            <div class="feature">
                <h3>Fast Shipping</h3>
                <p>Get your orders delivered quickly and efficiently.</p>
            </div>
            <div class="feature">
                <h3>24/7 Support</h3>
                <p>Our customer support team is available around the clock to assist you.</p>
            </div>
            <div class="feature">
                <h3>Secure Payments</h3>
                <p>Your transactions are always safe with us.</p>
            </div>
        </div>
    </section>
</div>

   <section class="customer-ratings-container">
    <div class="left-side">
        <img src="https://images.squarespace-cdn.com/content/v1/5186f53fe4b0c64b3102c2e6/1482251586892-V91MVYMAT3XCZUG1RIXV/image-asset.jpeg" alt="Customer Image" class="left-image">
    </div>
    <div class="right-side">
        <h2>Customer Ratings</h2>
        <p>Here's what our customers have to say about us:</p>
        <div id="customerCarousel" class="carousel slide" data-bs-ride="carousel">
            <div class="carousel-inner">
                <div class="carousel-item active">
                    <div class="rating">
                        <h3>John Doe</h3>
                        <p>&#9733;&#9733;&#9733;&#9733;&#9733;</p>
                        <p>"The best IoT devices I have purchased! Fast shipping and excellent customer service."</p>
                    </div>
                </div>
                <div class="carousel-item">
                    <div class="rating">
                        <h3>Jane Smith</h3>
                        <p>&#9733;&#9733;&#9733;&#9733;&#9733;</p>
                        <p>"I love the variety of products. Easy to use and fast delivery. Highly recommend!"</p>
                    </div>
                </div>
                <div class="carousel-item">
                    <div class="rating">
                        <h3>Michael Johnson</h3>
                        <p>&#9733;&#9733;&#9733;&#9733;</p>
                        <p>"Good selection of products, but shipping took a little longer than expected."</p>
                    </div>
                </div>
                <div class="carousel-item">
                    <div class="rating">
                        <h3>Bob Blue</h3>
                        <p>&#9733;&#9733;&#9733;&#9733;</p>
                        <p>"Great variety of high-quality products! Had to wait a bit for shipping."</p>
                    </div>
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#customerCarousel" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" ></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#customerCarousel" data-bs-slide="next">
                <span class="carousel-control-next-icon" ></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
    </div>
</section>



 




           <section class="customer-support">
    <h2>Customer Support</h2>
    <p>Need help? We're here to assist you!</p>
    <div class="support-options">
        <div class="support-option">
            <h3>FAQs</h3>
            <p>Find answers to common questions</p>
            <a href="faq.jsp">Go to FAQs</a>
        </div>
        <div class="support-option">
            <h3>Contact Us</h3>
            <p>Have a question? Get in touch with our support team</p>
            <a href="contact.jsp">Contact Us</a>
        </div>
        <div class="support-option">
            <h3>Live Chat</h3>
            <p>Chat with our team for instant support</p>
            <button onclick="startLiveChat()">Start Live Chat</button>
        </div>
    </div>
</section>

        </div>
    </body>
</html>