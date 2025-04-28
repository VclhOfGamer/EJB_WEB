<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.ejb_web.model.Product" %>
<%@ page import="org.example.ejb_web.model.Category" %>
<%@ page import="java.util.List" %>
<%
    boolean isStaff = request.getAttribute("isStaff") != null && (boolean)request.getAttribute("isStaff");
    String backUrl = isStaff ? "StaffProduct" : "Product";
    String formAction = isStaff ? "StaffEditProduct" : "editProduct";


    if(isStaff) {
%>
<jsp:include page="../StaffView/staffincludes/upperpart.jsp" />
<% } else { %>
<jsp:include page="../includes/upperpart.jsp" />
<% } %>

<title>Edit Product</title>

<section class="animate__animated animate__fadeIn">
    <h2>Edit Product</h2>
    <hr>
    <a class="btn btn-primary" href="<%= backUrl %>" role="button">< Back</a>
    <br><br>
    <div class="card border">
        <div class="card-body">
            <% Product product = (Product) request.getAttribute("product");
                List<Category> categoryList = (List<Category>) request.getAttribute("categories"); %>
            <form action="<%= formAction %>" method="post">
                <input type="hidden" name="id" value="<%= product.getId() %>">
                <p>Product Name:
                    <input required type="text" class="form-control" name="name" value="<%= product.getName() %>">
                </p>
                <p>Quantity:
                    <input required type="number" min="0" class="form-control" name="quantity" value="<%= product.getQuantity() %>">
                </p>
                <p>Price:
                    <input required type="number" min="0" step="0.01" class="form-control" name="price" value="<%= product.getPrice() %>">
                </p>
                <p>Unit:
                    <input required type="text" class="form-control" name="unit" value="<%= product.getUnit() %>">
                </p>

                <% if (isStaff) { %>
                <!-- For staff users, display staff name and use hidden staffId from session -->
                <p>Staff:
                    <input readonly type="text" class="form-control" value="${staffName}">
                </p>
                <% } else { %>
                <!-- For admin users, allow editing of staff ID -->
                <p>Staff ID:
                    <input required type="number" min="1" class="form-control" name="addedBy" value="<%= product.getAddedBy() %>">
                </p>
                <% } %>

                <p>Category:
                    <select class="form-select" name="categoryId">
                        <% for (Category cat : categoryList) { %>
                        <option value="<%= cat.getId() %>" <%= cat.getId() == product.getCategoryId() ? "selected" : "" %>>
                            <%= cat.getName() %>
                        </option>
                        <% } %>
                    </select>
                </p>
                <button class="btn btn-success" type="submit">Save Changes</button>
            </form>
        </div>
    </div>
</section>

<% if(isStaff) { %>
<jsp:include page="../StaffView/staffincludes/lowerpart.jsp" />
<% } else { %>
<jsp:include page="../includes/lowerpart.jsp" />
<% } %>