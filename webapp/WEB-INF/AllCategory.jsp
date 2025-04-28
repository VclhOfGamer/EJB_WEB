<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.ejb_web.model.Category" %>
<%@ page import="java.util.List, java.util.Map" %>
<jsp:include page="includes/upperpart.jsp" />
<title>Category</title>

<section class="animate__animated animate__fadeIn">
    <h2>Category</h2>
    <hr>
    <a href="addCategory" class="btn btn-primary" role="button">+ Add Category</a>
    <br><br>

    <input type="search" placeholder="Search Category..." class="form-control search-input border border-dark" data-table="table-list" />
    <br>

    <div class="table-responsive">
        <table class="table table-sm table-striped table-hover text-center table-list" style="font-size:15px;">
            <thead class="text-light bg-info">
            <tr>
                <th>ID</th>
                <th>Category Name</th>
                <th>Products</th>
                <th>Action</th>
            </tr>
            </thead>

            <%
                List<Category> categories = (List<Category>) request.getAttribute("categoryList");
                Map<Integer, Integer> productCounts = (Map<Integer, Integer>) request.getAttribute("productCounts");

                for (Category category : categories) {
                    int productCount = productCounts.getOrDefault(category.getId(), 0);
            %>
            <tr>
                <td><%= category.getId() %></td>
                <td><%= category.getName() %></td>
                <td><%= productCount %></td>
                <td>
                    <a class="btn btn-sm btn-primary text-white" href="editCategory?id=<%= category.getId() %>"><i class="fas fa-edit"></i></a>
                    <a class="btn btn-sm btn-danger text-white" href="deleteCategory?id=<%= category.getId() %>"><i class="fas fa-trash-alt"></i></a>
                </td>
            </tr>
            <%
                }
            %>
        </table>
    </div>
</section>
<jsp:include page="includes/lowerpart.jsp" />
