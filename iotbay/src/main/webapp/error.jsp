<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex justify-content-center align-items-center vh-100">
    <div class="text-center">
        <h1 class="text-danger">500 - Something Went Wrong</h1>
        <p class="lead">An unexpected error occurred. Please try again later.</p>
        <% if (exception != null) { %>
            <div class="alert alert-warning text-start mt-4 mx-auto" style="max-width: 600px;">
                <strong>Error Details:</strong><br/>
                <%= exception.getMessage() %><br/>
                <pre><%= exception.toString() %></pre>
            </div>
        <% } %>
        <a href="index.jsp" class="btn btn-primary mt-3">Return to Home</a>
    </div>
</body>
</html>
