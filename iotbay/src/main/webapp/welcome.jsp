<%@ page import="model.User, model.Customer, model.Staff" %>
<html>
<jsp:include page="/ConnServlet" flush="true"/>
<%!
    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    User user      = (User) session.getAttribute("user");
    boolean isAdmin = (user instanceof Staff) && ((Staff)user).isAdmin();
    boolean isStaff = user instanceof Staff;
    String firstName = user.getFirstName() == null ? "" : user.getFirstName();
%>
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
    <div class="container">
        <div class="raisedbox">
            <div class="check-icon text-center">
                <i class="fas fa-check-circle"></i>
            </div>
            <h2 class="animated-title">
                Welcome<%= isNullOrEmpty(firstName) ? "" : ", " + firstName %>!
            </h2>
            <p>Click <a href="index.jsp">here</a> to proceed to the main page.</p>
            <p>Click <a href="account.jsp">here</a> to view your account details.</p>
        </div>
    </div>
</body>
</html>
