<nav id="sidebarMenu" class="collapse d-lg-block sidebar collapse bg-dark text-light">
    <div class="position-sticky">
        <div class="list-group list-group-flush mx-3 mt-4">
            <a id="dashboard" href="StaffDashboard" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-tachometer-alt fa-fw me-3"></i><span>Dashboard</span>
            </a>
            <a id="product" href="StaffProduct" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-box fa-fw me-3"></i><span>Product</span>
            </a>
            <a id="orders" href="StaffOrder" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-chart-line fa-fw me-3"></i><span>Orders</span>
            </a>
            <a id="staff" href="StaffProfile" class="list-group-item list-group-item-action py-2 ripple bg-dark text-light">
                <i class="fas fa-user fa-fw me-3"></i><span>Staff Profile</span>
            </a>
        </div>
    </div>

    <%
        String staffDisplay = (String) request.getAttribute("staffDisplay");
        if (staffDisplay != null && !staffDisplay.isEmpty()) {
    %>
    <div class="fixed-bottom mx-3 mt-4 text-end">
        <label>Hello, <%= staffDisplay %></label><br>
        <small>Signed in as Staff</small>
    </div>
    <% } %>
</nav>

<script>
    const page = window.location.pathname.split("/").pop();

    const pageMap = {
        "StaffDashboard": "dashboard",
        "StaffProduct": "product",
        "StaffOrder": "orders",
        "viewStaffOrder": "orders",
        "editOrderProduct": "orders",
        "updatePayOrder": "orders",
        "deleteOrderProduct": "orders",
        "StaffProfile": "staff"
    };

    const id = pageMap[page];
    if (id) {
        document.getElementById(id)?.classList.add("bg-danger");
        document.getElementById(id)?.classList.remove("bg-dark");
    }
</script>
