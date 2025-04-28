<%@ page import="org.example.ejb_web.model.Admin" %>
<jsp:include page="../includes/upperpart.jsp" />

<%
    Admin admin = (Admin) request.getAttribute("admin");
    if (admin == null) {
%>
<div class="alert alert-danger">Không tìm thấy admin cần chỉnh sửa.</div>
<%
        return;
    }
%>

<title>Edit Administrator</title>

<section class="animate__animated animate__fadeIn">
    <h2>Edit Administrator</h2>
    <hr>
    <a class="btn btn-primary" href="Admin" role="button">&lt; Back</a>
    <br><br>



    <div class="card border">
        <div class="card-body">
            <form action="editAdmin" method="post">
                <p>ID:<input readonly class="form-control text-secondary" type="text" name="adminId" value="<%= admin.getId() %>" /></p>
                <p>Name:<input required class="form-control" type="text" name="name" value="<%= admin.getName() %>" /></p>
                <p>Username:<input required class="form-control" type="text" name="username" value="<%= admin.getUsername() %>" /></p>
                <p>Password:<input required class="form-control" type="password" name="password" value="<%= admin.getPassword() %>" /></p>
                <button type="submit" class="btn btn-success">Save</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../includes/lowerpart.jsp" />
