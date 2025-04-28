<nav id="sidebarMenu" class="collapse d-lg-block sidebar collapse bg-dark text-light">
    <div class="position-sticky">
        <div class="list-group list-group-flush mx-3 mt-4">
            <a id="dashboard" href="AdminDashboard" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-tachometer-alt fa-fw me-3"></i><span>Dashboard</span>
            </a>
            <a id="category" href="Category" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-list fa-fw me-3"></i><span>Category </span>
            </a>
            <a id="product" href="Product" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light"><i
                    class="fas fa-box fa-fw me-3"></i><span>Product</span></a>
            <a id="orders" href="Order" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light"><i
                    class="fas fa-chart-line fa-fw me-3"></i><span>Orders</span></a>
            <a id="staff" href="Staff" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-user fa-fw me-3"></i><span>Staff</span>
            </a>
            <a id="admin" href="Admin" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-user-shield fa-fw me-3"></i><span>Administrator</span>
            </a>
            <a id="predictModel" href="PredictModel" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-brain fa-fw me-3"></i><span>Predict Model</span>
            </a>
        </div>
    </div>

    <%-- Hiển thị tên admin đã truyền từ Controller --%>
    <%
        String adminDisplay = (String) request.getAttribute("adminDisplay");
        if (adminDisplay != null && !adminDisplay.isEmpty()) {
    %>
    <div class="fixed-bottom mx-3 mt-4 text-end">
        <label>Hello, <%= adminDisplay %></label><br>
        <small>Signed in as Admin</small>
    </div>
    <% } %>


</nav>

<script>
    const page = window.location.pathname.split("/").pop();

    const pageMap = {
        "Dashboard": "dashboard",
        "Category": "category",
        "addCategory": "category",
        "editCategory": "category",
        "deleteCategory": "category",
        "Product": "product",
        "addProduct": "product",
        "editProduct": "product",
        "deleteProduct": "product",
        "Order": "orders",
        "viewOrder": "orders",
        "editOrderProduct": "orders",
        "updatePayOrder": "orders",
        "deleteOrderProduct": "orders",
        "Staff": "staff",
        "addStaff": "staff",
        "editStaff": "staff",
        "deleteStaff": "staff",
        "Admin": "admin",
        "addAdmin": "admin",
        "editAdmin": "admin",
        "deleteAdmin": "admin",
        "PredictModel.jsp": "predictModel"
    };

    const id = pageMap[page];
    if (id) {
        document.getElementById(id)?.classList.add("bg-danger");
        document.getElementById(id)?.classList.remove("bg-dark");
    }
</script>
