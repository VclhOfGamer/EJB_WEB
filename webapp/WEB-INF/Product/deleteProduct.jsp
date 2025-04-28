<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.ejb_web.model.Product" %>
<%
    boolean isStaff = request.getAttribute("isStaff") != null && (boolean)request.getAttribute("isStaff");
    String backUrl = isStaff ? "StaffProduct" : "Product";
    String formAction = isStaff ? "StaffDeleteProduct" : "deleteProduct";

    // Include the appropriate upperpart based on user type
    if(isStaff) {
%>
<jsp:include page="../StaffView/staffincludes/upperpart.jsp" />
<% } else { %>
<jsp:include page="../includes/upperpart.jsp" />
<% } %>

<title>Delete Product</title>

<section class="animate__animated animate__fadeIn">
    <h2>Delete Product</h2>
    <hr>
    <a class="btn btn-primary" href="<%= backUrl %>" role="button">< Back</a>
    <br><br>
    <div class="card border border-danger">
        <div class="card-body">
            <div class="alert alert-danger animate__animated animate__shakeX" role="alert">
                <i class="fas fa-exclamation-triangle"></i>&nbsp; Are you sure you want to delete this product?
            </div>
            <%
                Product product = (Product) request.getAttribute("product");
                if (product != null) {
            %>
            <form action="<%= formAction %>" method="post">
                <!-- Ensure the ID is properly set -->
                <input type="hidden" name="id" value="<%= product.getId() %>">
                <p>Product Name:
                    <input readonly type="text" class="form-control" value="<%= product.getName() %>">
                </p>
                <p>Quantity:
                    <input readonly type="number" class="form-control" value="<%= product.getQuantity() %>">
                </p>
                <p>Price:
                    <input readonly type="number" step="0.01" class="form-control" value="<%= product.getPrice() %>">
                </p>
                <p>Unit:
                    <input readonly type="text" class="form-control" value="<%= product.getUnit() %>">
                </p>
                <p>Category:
                    <input readonly type="text" class="form-control" value="<%= product.getCategoryName() != null ? product.getCategoryName() : "Unknown Category" %>">
                </p>

                <% if (isStaff) { %>
                <!-- For staff users, display staff name -->
                <p>Staff:
                    <input readonly type="text" class="form-control" value="${staffName}">
                </p>
                <% } else { %>
                <!-- For admin users, display the staff ID -->
                <p>Staff ID:
                    <input readonly type="text" class="form-control" value="<%= product.getAddedBy() %>">
                </p>
                <% } %>

                <button type="submit" class="btn btn-danger">Confirm Delete</button>
            </form>
            <% } else { %>
            <div class="alert alert-warning">
                <i class="fas fa-exclamation-circle"></i>&nbsp; Product not found.
                <br><br>
                <a href="<%= backUrl %>" class="btn btn-primary">Return to Products</a>
            </div>
            <% } %>
        </div>
    </div>
</section>

<% if(isStaff) { %>
<jsp:include page="../StaffView/staffincludes/lowerpart.jsp" />
<% } else { %>
<jsp:include page="../includes/lowerpart.jsp" />
<% } %>