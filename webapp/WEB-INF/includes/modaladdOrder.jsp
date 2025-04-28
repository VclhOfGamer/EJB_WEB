<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Modal pour ajouter une commande -->
<div class="modal fade" id="addOrderModal" tabindex="-1" aria-labelledby="addOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addOrderModalLabel">Create New Order</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="Order" method="post" onsubmit="return validateOrderForm()">
                <div class="modal-body">
                    <input type="hidden" name="action" value="addOrder">
                    <input type="hidden" name="staffId" id="staffId">

                    <div class="mb-3">
                        <label for="inputStaffId" class="form-label">Staff ID:</label>
                        <input type="number" class="form-control" id="inputStaffId" onchange="updateStaffInfo()" required>
                    </div>

                    <div class="mb-3">
                        <label for="staffInfo" class="form-label">Staff Info:</label>
                        <div id="staffInfo" class="form-control bg-light" style="min-height: 38px;"></div>
                    </div>

                    <div class="mb-3">
                        <label for="orderType" class="form-label">Order Type:</label>
                        <select class="form-select" id="orderType" name="orderType" required>
                            <option value="DELIVER">DELIVER</option>
                            <option value="RECEIVE">RECEIVE</option>
                        </select>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="createOrderBtn">Create Order</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Tạo danh sách nhân viên từ dữ liệu server trả về
    const staffList = [
        <c:forEach var="staff" items="${staffList}" varStatus="loop">
        {
            id: ${staff.id},
            name: "${staff.name}"
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];

    function updateStaffInfo() {
        const inputId = parseInt(document.getElementById("inputStaffId").value);
        const infoBox = document.getElementById("staffInfo");
        const createOrderBtn = document.getElementById("createOrderBtn");
        const staffIdField = document.getElementById("staffId");

        const staff = staffList.find(s => s.id === inputId);

        if (staff) {
            // Hiển thị thông tin nhân viên
            infoBox.innerHTML = "<strong>" + staff.name + "</strong>";
            infoBox.classList.remove("text-danger");
            staffIdField.value = staff.id;
            createOrderBtn.disabled = false;
        } else {
            infoBox.innerHTML = "<span class='text-danger'>Staff not found with this ID</span>";
            staffIdField.value = "";
            createOrderBtn.disabled = true;
        }
    }

    function validateOrderForm() {
        const staffId = document.getElementById("staffId").value;

        if (!staffId || staffId === "") {
            alert("Please enter a valid Staff ID.");
            return false;
        }

        return true;
    }
</script>