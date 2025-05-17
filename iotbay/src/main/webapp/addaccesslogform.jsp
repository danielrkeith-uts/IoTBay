<%@ page import="model.User, model.Enums.ApplicationAction" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !(user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF)) {
    response.sendRedirect("unauthorized.jsp");
    return;
}
%>

<html>
<head>
    <title>Add Access Log</title>
</head>
<body>
    <h1>Add Access Log</h1>

    <form action="AddAccessLogServlet" method="post">
        <label for="applicationAction">Action:</label>
        <select name="applicationAction" required>
            <option value="LOGIN">LOGIN</option>
            <option value="LOGOUT">LOGOUT</option>
            <option value="REGISTER">REGISTER</option>
            <option value="ADD_TO_CART">ADD TO CART</option>
            <option value="PLACE_ORDER">PLACE ORDER</option>
        </select><br/>
        
        <label for="userId">User ID:</label>
        <input type="text" name="userId" required/><br/>

        <button type="submit">Add Log</button>
    </form>
</body>
</html>
