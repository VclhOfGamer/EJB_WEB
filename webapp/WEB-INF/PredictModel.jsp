<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.ejb_web.model.Prediction" %>
<jsp:include page="includes/upperpart.jsp" />
<jsp:include page="includes/cardbg.jsp" />
<title>Predicted Products</title>

<section class="animate__animated animate__fadeIn">
    <h2>Predicted Product Quantity</h2>
    <hr>

    <div class="table-responsive">
        <table id="predictTable" class="table table-sm table-striped table-hover text-center table-list" style="font-size:15px; width:100%">
            <thead>
            <tr>
                <th>Product ID</th>
                <th>Product Name</th>
                <th>Category</th>
                <th>Predicted Quantity</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <% for (Prediction p : (List<Prediction>) request.getAttribute("predictions")) { %>
            <tr>
                <td><%= p.getProductId() %></td>
                <td><%= p.getProductName() %></td>
                <td><%= p.getCategory() %></td>
                <td><%= p.getQuantityPredicted() %></td>
                <td>
                    <form action="PredictModel" method="POST" class="form-inline" style="display:inline;">
                        <input type="hidden" name="productId" value="<%= p.getProductId() %>">

                        <!-- Nhập số lượng với class bootstrap -->
                        <div class="form-group mr-2">
                            <input type="number" name="quantity" value="<%= p.getQuantityPredicted() %>" min="0" class="form-control form-control-sm" style="width: 80px;">
                        </div>

                        <!-- Nút Edit và Delete với các class bootstrap -->
                        <button type="submit" name="action" value="edit" class="btn btn-warning btn-sm mr-2">Edit</button>
                        <button type="submit" name="action" value="delete" class="btn btn-danger btn-sm">Delete</button>
                    </form>
                </td>

            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</section>

<jsp:include page="includes/lowerpart.jsp" />
