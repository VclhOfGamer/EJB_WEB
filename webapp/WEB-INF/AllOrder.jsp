<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.ejb_web.model.Order" %>
<jsp:include page="includes/upperpart.jsp" />
<jsp:include page="includes/cardbg.jsp" />
<%@ page import="java.text.SimpleDateFormat" %>
<jsp:include page="includes/modaladdOrder.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

<title>Order</title>

<section class="animate__animated animate__fadeIn">
    <h2>Order</h2>
    <hr>
    <!-- Nút Add Order -->
    <div class="mb-3">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addOrderModal">+ Add Order</button>
    </div>
    <!-- Thông tin tổng quan -->
    <%
        int totalOrders = (int) request.getAttribute("totalOrders");
        double paidSales = (double) request.getAttribute("paidSales");
        double unpaidSales = (double) request.getAttribute("unpaidSales");
    %>

    <div class="row text-light">
        <div class="col">
            <div class="card border text-center bg-c-green">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center"><i class="fas fa-boxes fa-3x"></i></div>
                        <div class="text-end">
                            <label style="font-size:25px;"><%= totalOrders %></label>
                            <p class="mb-0">Total Orders</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col">
            <div class="card border text-center bg-c-yellow">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center"><i class="fas fa-box fa-3x"></i></div>
                        <div class="text-end">
                            <label style="font-size:25px;"><%= paidSales %></label>
                            <p class="mb-0">Paid Sales</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col">
            <div class="card border text-center bg-c-pink">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center"><i class="fas fa-box-open fa-3x"></i></div>
                        <div class="text-end">
                            <label style="font-size:25px;" class=""><%= unpaidSales %></label>
                            <p class="mb-0">Unpaid Sales</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <br>
    <!-- Bảng đơn hàng -->
    <div class="table-responsive">
        <table id="orderTable" class="table table-sm table-striped table-hover text-center table-list" style="font-size:15px; width:100%">
            <thead class="text-light bg-info">
            <tr>
                <th style="align-items: center">ID</th>
                <th style="align-items: center">Order Type</th>
                <th style="align-items: center">Status</th>
                <th style="align-items: center">Order Date</th>
                <th style="align-items: center">Staff ID</th>
                <th style="align-items: center">Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Order> orders = (List<Order>) request.getAttribute("orders");
                for (Order o : orders) {
            %>
            <tr>
                <td><%= o.getId() %></td>
                <td><%= o.getOrderType() %></td>
                <td><%= o.getStatus() %></td>
                <%
                    String formattedDate = "N/A";
                    if (o.getOrderDate() != null) {
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        formattedDate = o.getOrderDate().format(formatter);
                    }
                %>
                <td><%= formattedDate %></td>
                <td><%= o.getStaffId() %></td>
                <td>
                    <!-- Thêm nút "View" để xem chi tiết đơn hàng -->
                    <a href="viewOrder?orderid=<%= o.getId() %>" class="btn btn-info">View</a>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>

    <!-- Stylesheet -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css" />
    <link rel="stylesheet" href="https://cdn.datatables.net/searchpanes/2.2.0/css/searchPanes.dataTables.min.css" />
    <link rel="stylesheet" href="https://cdn.datatables.net/select/1.7.0/css/select.dataTables.min.css" />

    <!-- JS libraries -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/searchpanes/2.2.0/js/dataTables.searchPanes.min.js"></script>
    <script src="https://cdn.datatables.net/select/1.7.0/js/dataTables.select.min.js"></script>

    <script>
        $(document).ready(function () {
            const table = $('#orderTable').DataTable({
                pageLength: 10,
                order: [[0, 'asc'], [3, 'asc']],
                dom: '<"row mb-3"<"col-md-12"f>>rt<"searchPanesContainer mt-4"Pl><"row mt-3"<"col-md-6"p>>',
                columnDefs: [
                    { targets: [0, 3], orderable: true },
                    {
                        targets: [1, 2, 4],
                        orderable: false,
                        searchable: true,
                        searchPanes: {
                            show: true,
                            controls: true
                        }
                    }
                ],
                searchPanes: {
                    layout: 'columns-3',
                    cascadePanes: false,
                    dtOpts: {
                        select: {
                            style: 'multi'
                        }
                    }
                },
                language: {
                    search: "",
                    lengthMenu: "",
                    info: "",
                    zeroRecords: "Không tìm thấy đơn hàng phù hợp",
                    paginate: {
                        first: "Đầu",
                        last: "Cuối",
                        next: "Tiếp",
                        previous: "Trước"
                    }
                }
            });

            // Sửa vị trí của thanh tìm kiếm
            $("div.dataTables_filter input")
                .attr("placeholder", "Search...")
                .addClass("form-control mb-3")
                .css({ width: "100%", "max-width": "100%" });
            $('div.dataTables_filter').addClass('w-100 mb-3');
            $('div.dataTables_filter label').addClass('w-100');
            $('div.dataTables_filter input')
                .addClass('form-control')
                .css({
                    width: '99%',
                    'max-width': '99%'
                });
        });
    </script>
</section>
