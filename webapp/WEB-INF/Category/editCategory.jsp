<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.ejb_web.model.Category" %>
<jsp:include page="../includes/upperpart.jsp" />
<title>Edit Category</title>

<section class="animate__animated animate__fadeIn">

    <h2>Edit Category</h2>
    <hr>
    <a class="btn btn-primary" href="Category" role="button">&lt; Back</a>
    <br><br>

    <%
        Category category = (Category) request.getAttribute("category");
        if (category == null) {
    %>
    <div class="alert alert-danger">Không tìm thấy danh mục cần sửa.</div>
    <%
    } else {
    %>

    <div class="card border">
        <div class="card-body">
            <form action="editCategory" method="post">
                <div class="form-group">
                    <label style="font-size:15px;">Category ID :</label>
                    <input readonly type="text" value="<%= category.getId() %>" class="form-control" required name="catId">
                </div>
                <br>
                <div class="form-group">
                    <label style="font-size:15px;">Category Name :</label>
                    <input type="text" value="<%= category.getName() %>" class="form-control" required name="catName">
                </div>
                <br>
                <button type="submit" class="btn btn-success">Save</button>
            </form>
        </div>
    </div>

    <%
        }
    %>

</section>

<jsp:include page="../includes/lowerpart.jsp" />
