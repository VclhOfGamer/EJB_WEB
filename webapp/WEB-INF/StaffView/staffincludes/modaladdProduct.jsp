<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script type="text/javascript">
    function OpenBootstrapPopup() {
        $("#addOPModal").modal('show');
    }

    const products = [
        <c:forEach var="p" items="${productList}" varStatus="loop">
        {
            id: ${p.id},
            name: "${p.name}",
            price: ${p.price},
            quantity: ${p.quantity},
            unit: "${p.unit}"
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];


    function updateProductInfo() {
        const inputId = parseInt(document.getElementById("inputProdId").value);
        const infoBox = document.getElementById("productInfo");
        const qtyInput = document.getElementById("quantity");

        const product = products.find(p => p.id == inputId);

        if (product) {
            // Format price as Vietnamese currency
            let price;
            price = product.price + " $"; // Fallback if formatting fails


            // Set HTML content directly with basic HTML
            infoBox.innerHTML = "<strong>" + product.name + "</strong><br>" +
                "Giá: " + price + " / " + product.unit + "<br>" +
                "Còn lại: " + product.quantity;

            // Set other form fields
            qtyInput.placeholder = "Max: " + product.quantity;
            qtyInput.max = product.quantity;
            document.getElementById("productId").value = product.id;
        } else {
            infoBox.innerHTML = "<span class='text-danger'>Không tìm thấy sản phẩm với ID này.</span>";
            qtyInput.placeholder = "Quantity";
            qtyInput.removeAttribute("max");
        }
    }



    function validateForm() {
        const productId = document.getElementById("productId").value;
        const quantity = document.getElementById("quantity").value;

        if (!productId || productId === "") {
            alert("Vui lòng nhập Product ID hợp lệ.");
            return false;
        }

        if (!quantity || quantity <= 0) {
            alert("Vui lòng nhập số lượng hợp lệ.");
            return false;
        }

        return true;
    }

    window.onload = OpenBootstrapPopup;
</script>

<!-- Modal -->
<div class="modal fade" id="addOPModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-dark" id="exampleModalLabel">
                    Add Product to Order ${order.orderType} - ${order.id}
                </h5>
            </div>

            <form action="StaffViewOrder" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="StaffAddProduct" />
                <input type="hidden" name="orderId" value="${order.id}" />
                <input type="hidden" name="productId" id="productId" />

                <div class="modal-body">
                    <label for="inputProdId">Nhập Product ID:</label>
                    <input type="number" class="form-control" id="inputProdId" onchange="updateProductInfo()" required />

                    <div id="productInfo" class="mt-2 text-dark"></div>

                    <label for="quantity" class="mt-3">Quantity:</label>
                    <input required min="1" type="number" class="form-control"
                           name="quantity" id="quantity"
                           placeholder="Nhập số lượng" />
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Add</button>
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Cancel</button>
                </div>
            </form>

        </div>
    </div>
</div>