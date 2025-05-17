<%@ page import="model.ApplicationAccessLog" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    ApplicationAccessLog log = (ApplicationAccessLog) request.getAttribute("log");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDate = sdf.format(log.getDateTime());
%>

<html>
<head>
    <title>Edit Access Log</title>
</head>
<body>
    <h1>Edit Access Log</h1>
    <form method="post" action="UpdateAccessLogServlet">
        <input type="hidden" name="logId" value="<%= log.getLogId() %>" />

        <label>Date and Time:</label>
        <input type="text" name="dateTime" value="<%= formattedDate %>" readonly /><br/>

        <label>Action:</label>
        <select name="applicationAction">
            <option value="LOGIN" <%= log.getApplicationAction().name().equals("LOGIN") ? "selected" : "" %>>LOGIN</option>
            <option value="LOGOUT" <%= log.getApplicationAction().name().equals("LOGOUT") ? "selected" : "" %>>LOGOUT</option>
            <option value="REGISTER" <%= log.getApplicationAction().name().equals("REGISTER") ? "selected" : "" %>>REGISTER</option>
            <option value="ADD_TO_CART" <%= log.getApplicationAction().name().equals("ADD_TO_CART") ? "selected" : "" %>>ADD TO CART</option>
            <option value="PLACE_ORDER" <%= log.getApplicationAction().name().equals("PLACE_ORDER") ? "selected" : "" %>>PLACE ORDER</option>
        </select><br/>

        <button type="submit">Save</button>
    </form>
</body>
</html>
