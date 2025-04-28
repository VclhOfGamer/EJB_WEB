<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="alert alert-danger">
        <h4>Error Occurred</h4>
        <p>${errorMessage != null ? errorMessage : 'An unknown error occurred'}</p>

        <!-- Show technical details if available -->
        <% if (exception != null) { %>
        <hr>
        <details>
            <summary>Technical Details</summary>
            <pre><%= exception.getMessage() %></pre>
        </details>
        <% } %>
    </div>
    <div class="mt-3">
        <a href="Order" class="btn btn-primary">Return to Orders</a>
    </div>
</div>
</body>
</html>