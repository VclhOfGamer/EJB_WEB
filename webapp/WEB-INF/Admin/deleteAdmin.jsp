<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../includes/upperpart.jsp" />
<title>Delete Administrator</title>

<section class="animate__animated animate__fadeIn">
    <h2>Delete Administrator</h2>
    <hr>

    <a class="btn btn-primary" href="Admin" role="button">< Back</a>
    <br><br>

    <%
        org.example.ejb_web.model.Admin admin = (org.example.ejb_web.model.Admin) request.getAttribute("admin");
        if (admin == null) {
    %>
    <div class="alert alert-danger">Không tìm thấy Admin cần xóa!</div>
    <%
            return;
        }
    %>

    <div class="card border border-danger">
        <div class="card-body">
            <div class="alert alert-danger animate__animated animate__shakeX" role="alert">
                <i class="fas fa-exclamation-triangle"></i>&nbsp;
                Are you sure to delete the following administrator?
            </div>

            <form action="deleteAdmin" method="post">
                <p>ID:<input readonly class="form-control text-danger" type="text" name="adminId" value="<%= admin.getId() %>" /></p>
                <p>Name:<input disabled class="form-control" type="text" value="<%= admin.getName() %>" /></p>
                <p>Username:<input disabled class="form-control" type="text" value="<%= admin.getUsername() %>" /></p>
                <p>Password:<input disabled class="form-control" type="password" value="<%= admin.getPassword() %>" /></p>
                <button type="submit" class="btn btn-danger">Delete</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../includes/lowerpart.jsp" />
