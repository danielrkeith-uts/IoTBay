<%@ page import="model.User"%>

<html>

<%
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");
    String password = request.getParameter("password");
    String gender = request.getParameter("gender");
    String tos = request.getParameter("tos");
    User user = new User(firstName, lastName, email, phone, password, gender);
    session.setAttribute("user", user);
%>

<body>

    <% 
        if (tos != null && tos.equals("on")) {
    %>
        <h1>Welcome, <%= firstName %>!</h1>
        <h1>Your last name is <%= lastName %>.</h1>
        <h1>Your email is <%= email %>.</h1>
        <h1>Your phone number is <%= phone %>.</h1>
        <p>Your password is <%= password %>.</p>
        <p>Your gender is <%= gender %>.</p>
        <p>Click <a href="index.jsp">here</a> to proceed to the main page.</p>

    <% 
        } else { 
    %>
        <h1>
        Sorry, you must agree to the Terms of Service. </br>
        Click <a href="register.jsp">here</a> to go back.</br>
        </h1>
    <% 
        }
    %>
</body>
</html>