<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../includes/upperpart.jsp" />
<title>View Order</title>

<!-- Modal tự động bật lại nếu có action -->
<c:if test="${modalAction == 'edit'}">
    <jsp:include page="../includes/modalEditOP.jsp" />
</c:if>
<c:if test="${modalAction == 'add'}">
    <jsp:include page="../includes/modaladdProduct.jsp" />
</c:if>
<c:if test="${modalAction == 'exist'}">
    <jsp:include page="../includes/modalProductExist.jsp" />
</c:if>
<c:if test="${modalAction == 'exceed'}">
    <jsp:include page="../includes/modalQtyExceed.jsp" />
</c:if>

<!-- Back Button -->
<a class="btn btn-primary" href="Order" role="button">← Back</a>

<div class="container mt-4">
    <h2>View Order</h2>
    <label>Order : ${order.orderType} - ${order.id}</label><br>
    <label>Date : ${order.orderDate}</label><br>
    <label>Created by : ${staff.name}</label><br>
    <label>Status : </label>
    <span class="badge ${order.status == 'PAID' ? 'badge-success' : 'badge-danger'}">${order.status}</span>
</div>

<div class="container mt-4">
    <h4>Order Items</h4>
    <table class="table table-hover table-bordered">
        <thead class="thead-dark">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Product</th>
            <th scope="col">Unit</th>
            <th scope="col">Quantity</th>
            <th scope="col">Price ($)</th>
            <th scope="col">Subtotal ($)</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="op" items="${orderItems}" varStatus="loop">
            <c:forEach var="p" items="${productList}">
                <c:if test="${p.id == op.productId}">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${p.name}</td>
                        <td>${p.unit}</td>
                        <td>${op.quantity}</td>
                        <td>${p.price}</td>
                        <td>${op.quantity * p.price}</td>
                        <td>
                            <c:if test="${order.status == 'UNPAID'}">
                                <a href="viewOrder?orderid=${order.id}&action=edit&opid=${op.id}" class="btn btn-warning btn-sm">Edit</a>
                                <a href="viewOrder?action=deleteProduct&productId=${op.productId}&orderId=${order.id}" class="btn btn-danger btn-sm" onclick="return confirm('Confirm delete this product?');">Delete</a>
                            </c:if>
                            <c:if test="${order.status == 'PAID'}">
                                <span class="text-muted">Locked</span>
                            </c:if>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="5" class="text-right font-weight-bold">Total:</td>
            <td colspan="2" class="font-weight-bold">$ ${totalPrice}</td>
        </tr>
        </tfoot>
    </table>

    <!-- Hiện nút "Add Product" nếu đơn chưa thanh toán -->
    <c:if test="${order.status == 'UNPAID'}">
        <a href="viewOrder?orderid=${order.id}&action=add" class="btn btn-success">Add Product</a>

        <!-- Nút xác nhận -->
        <form method="post" action="Order?action=confirmOrder" style="display:inline;">
            <input type="hidden" name="orderid" value="${order.id}" />
            <button type="submit" class="btn btn-primary" onclick="return confirm('Xác nhận đơn hàng này?');">Xác nhận đơn</button>
        </form>
    </c:if>
</div>

<jsp:include page="../includes/lowerpart.jsp" />
