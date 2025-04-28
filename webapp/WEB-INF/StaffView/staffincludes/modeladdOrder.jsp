<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Modal để thêm đơn hàng -->
<div class="modal fade" id="StaffAddOrderModal" tabindex="-1" aria-labelledby="addOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addOrderModalLabel">Tạo đơn hàng mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/StaffOrder" method="post">
                <div class="modal-body">
                    <input type="hidden" name="action" value="StaffAddOrder">

                    <div class="mb-3">
                        <label for="staffId" class="form-label">Nhân viên:</label>
                        <select class="form-select" id="staffId" name="staffId" required>
                            <option value="">-- Chọn nhân viên --</option>
                            <c:forEach var="staff" items="${staffList}">
                                <option value="${staff.id}">${staff.name} (ID: ${staff.id})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="orderType" class="form-label">Loại đơn hàng:</label>
                        <select class="form-select" id="orderType" name="orderType" required>
                            <option value="DELIVER">DELIVER</option>
                            <option value="RECEIVE">RECEIVE</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Tạo đơn hàng</button>
                </div>
            </form>
        </div>
    </div>
</div>