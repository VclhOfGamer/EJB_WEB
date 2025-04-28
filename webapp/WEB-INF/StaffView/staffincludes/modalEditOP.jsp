<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Edit Product Quantity</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<script type="text/javascript">
    window.onload = function () {
        $("#editOPModal").modal('show');
    };
</script>

<!-- Modal -->
<div class="modal fade" id="editOPModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-dark" id="editModalLabel">Edit Quantity</h5>
            </div>
            <form action="StaffViewOrder" method="post">
                <input type="hidden" name="action" value="StaffEditProduct" />

                <div class="modal-body">
                    <input type="hidden" name="opid" value="${editItem.id}" />
                    <input type="hidden" name="productId" value="${editItem.productId}" />
                    <input type="hidden" name="orderId" value="${editItem.orderId}" />

                    <label for="quantity" class="form-label">Quantity:</label>
                    <input type="number" min="0" class="form-control" name="quantity" id="quantity" value="${editItem.quantity}" required />
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Save</button>
                    <a href="StaffViewOrder?orderid=${editItem.orderId}" class="btn btn-danger">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>