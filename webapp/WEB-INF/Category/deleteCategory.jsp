<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.ejb_web.model.Category" %>
<jsp:include page="../includes/upperpart.jsp" />
<title>Delete Category</title>

<section class="animate__animated animate__fadeIn">

    <h2>Delete Category</h2>
    <hr>
    <a class="btn btn-primary" href="Category" role="button">&lt; Back</a>
    <br><br>

    <%
        Category category = (Category) request.getAttribute("category");
    %>

    <div class="card border border-danger">
        <div class="card-body">
            <div class="alert alert-danger animate__animated animate__shakeX" role="alert">
                <i class="fas fa-exclamation-triangle"></i>&nbsp;
                Are you sure to delete the following category?
            </div>
            <form action="deleteCategory" method="post">
                <div class="form-group">
                    <label style="font-size:15px;">Category ID :</label>
                    <input readonly type="text" value="<%= category.getId() %>" class="form-control text-danger" required name="catId">
                </div>
                <br>
                <div class="form-group">
                    <label style="font-size:15px;">Category Name :</label>
                    <input readonly type="text" value="<%= category.getName() %>" class="form-control" required name="catName">
                </div>
                <br>
                <button type="submit" class="btn btn-danger">Delete</button>
            </form>
        </div>
    </div>

</section>

<jsp:include page="../includes/lowerpart.jsp" />
