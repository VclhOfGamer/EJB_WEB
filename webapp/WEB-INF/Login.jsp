<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%
  // Ưu tiên lấy từ request attribute, nếu không có thì lấy từ parameter
  String type = (String) request.getAttribute("type");
  if (type == null) {
    type = request.getParameter("type");
    if (type == null) type = "admin";
  }

  boolean isAdmin = type.equalsIgnoreCase("admin");
  String actionURL = isAdmin ? "loginAdmin" : "loginStaff";
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%= isAdmin ? "Administrator" : "Staff" %> Login</title>
  <jsp:include page="<%= isAdmin ? "includes/head.jsp" : "StaffView/staffincludes/head.jsp" %>" />
</head>
<body style="background: linear-gradient(to right, #FF5370, #ff869a);">

<%-- Modal hiển thị lỗi nếu có --%>
<%
  String code = (String) request.getAttribute("code");
  if ("failLogin".equals(code)) {
%>
<jsp:include page="<%= isAdmin ? "includes/modalAdminLoginFail.jsp" : "StaffView/staffincludes/modalAdminLoginFail.jsp" %>" />
<%
  }
%>

<div class="container">
  <div class="row mt-4">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
      <div class="card border-0 shadow rounded-3 my-5">
        <div class="card-body p-4 p-sm-5">
          <div class="text-center mb-4">
            <img src="${pageContext.request.contextPath}/img/logo.png" height="90" alt="Logo" loading="lazy" />
            <h5 class="lead mt-3">Inventory Management System</h5>
          </div>
          <hr>

          <form action="<%= actionURL %>" method="post">
            <h5 class="text-center mb-4"><%= isAdmin ? "Admin Login" : "Staff Login" %></h5>

            <% if (isAdmin) { %>
            <div class="mb-3">
              <input required type="text" name="adminUsername" class="form-control" placeholder="Username" value="admin" />
            </div>
            <div class="mb-4">
              <input required type="password" name="adminPass" class="form-control" placeholder="Password" value="admin" />
            </div>
            <% } else { %>
            <div class="mb-3">
              <input required type="text" name="staffUsername" class="form-control" placeholder="Staff Username" value="staff1" />
            </div>
            <div class="mb-4">
              <input required type="password" name="staffPass" class="form-control" placeholder="Password" value="Test1" />
            </div>
            <% } %>

            <div class="d-grid mb-4">
              <button type="submit" class="btn btn-dark">Sign in</button>
            </div>

            <div class="text-center mb-2">
              <% if (isAdmin) { %>
              <a href="<%= request.getContextPath() %>/StaffLogin">Staff Login</a>
              <% } else { %>
              <a href="<%= request.getContextPath() %>/AdminLogin">Admin Login</a>
              <% } %>
            </div>

            <hr>
            <p class="text-center text-muted">© 2025 NEU</p>
          </form>

        </div>
      </div>
    </div>
  </div>
</div>

<script>
  // Hiển thị modal lỗi nếu có
  document.addEventListener("DOMContentLoaded", function() {
    <% if ("failLogin".equals(code)) { %>
    var errorModal = new bootstrap.Modal(document.getElementById('errorModal'));
    errorModal.show();
    <% } %>
  });
</script>

</body>
</html>