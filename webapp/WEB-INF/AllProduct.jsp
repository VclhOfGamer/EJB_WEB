<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.ejb_web.model.Product" %>
<jsp:include page="includes/upperpart.jsp" />
<jsp:include page="includes/cardbg.jsp" />
<title>Product</title>

<% if (request.getParameter("code") != null) { %>
<% if ("editSuccess".equals(request.getParameter("code"))) { %>
<jsp:include page="includes/modalEditSuccess.jsp" />
<% } else if ("deleteSuccess".equals(request.getParameter("code"))) { %>
<jsp:include page="includes/modalDeleteSuccess.jsp" />
<% } else if ("addSuccess".equals(request.getParameter("code"))) { %>
<jsp:include page="includes/modalAddSuccess.jsp" />
<% } %>
<% } %>

<section class="animate__animated animate__fadeIn">
  <h2>Product</h2>
  <hr>
  <a href="addProduct" class="btn btn-primary" role="button">+ Add Product</a>
  <br><br>

  <%
    List<Product> products = (List<Product>) request.getAttribute("products");
    int noStock = 0, lowStock = 0, inStock = 0;

    for (Product p : products) {
      int qty = p.getQuantity();
      if (qty == 0) noStock++;
      else if (qty < 30) lowStock++;
      else inStock++;
    }
  %>

  <div class="row text-light">
    <div class="col">
      <div class="card border text-center bg-c-green">
        <div class="card-body">
          <div class="d-flex justify-content-between px-md-1">
            <div class="align-self-center"><i class="fas fa-boxes fa-3x"></i></div>
            <div class="text-end">
              <label style="font-size:25px;"><%= inStock %></label>
              <p class="mb-0">In Stock</p>
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
              <label style="font-size:25px;"><%= lowStock %></label>
              <p class="mb-0">Low Stock</p>
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
              <label style="font-size:25px;" class=""><%= noStock %></label>
              <p class="mb-0">No Stock</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <br>
  <div class="table-responsive">
    <table id="productTable" class="table table-sm table-striped table-hover text-center table-list" style="font-size:15px; width:100%">
      <thead class="text-light bg-info">
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Quantity</th>
        <th>Price/unit</th>
        <th>Unit</th>
        <th>Category</th>
        <th>Added by</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <% for (Product p : products) { %>
      <tr>
        <td><%= p.getId() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getQuantity() %></td>
        <td>$<%= String.format("%.2f", p.getPrice()) %> / <%= p.getUnit() %></td>
        <td><%= p.getUnit() %></td>
        <td><%= p.getCategoryName() %></td>
        <td><%= p.getStaffName() %></td>
        <td>
          <a class="btn btn-sm btn-primary" href="editProduct?id=<%= p.getId() %>"><i class="fas fa-edit"></i></a>
          <a class="btn btn-sm btn-danger" href="deleteProduct?id=<%= p.getId() %>"><i class="fas fa-trash-alt"></i></a>
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
      const table = $('#productTable').DataTable({
        pageLength: 10,
        order: [[0, 'asc']], // sort theo ID
        dom: '<"row mb-3"<"col-md-12"f>>rt<"searchPanesContainer mt-4"Pl><"row mt-3"<"col-md-6"p>>',
        columnDefs: [
          { targets: [0, 1, 2, 3], orderable: true },
          {
            targets: [4, 5, 6],
            orderable: false,
            searchable: true,
            searchPanes: {
              show: true,
              controls: true
            }
          },
          { targets: -1, orderable: false, searchable: false }
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
          lengthMenu: "", // Ẩn "Hiển thị x dòng"
          info: "",       // Ẩn "Hiển thị từ x đến y"
          zeroRecords: "Không tìm thấy sản phẩm phù hợp",
          paginate: {
            first: "Đầu",
            last: "Cuối",
            next: "Tiếp",
            previous: "Trước"
          }
        }
      });

      // Tùy chỉnh ô tìm kiếm thành full width + placeholder
      $("div.dataTables_filter input")
              .attr("placeholder", "Search by name...")
              .addClass("form-control mb-3")
              .css({ width: "100%", "max-width": "100%" });
      $('div.dataTables_filter').addClass('w-100 mb-3');
      $('div.dataTables_filter label').addClass('w-100');
      $('div.dataTables_filter input')
              .attr('placeholder', 'Search by name...')
              .addClass('form-control')
              .css({
                width: '99%',
                'max-width': '99%'
              });
    });
  </script>



  <style>
    tfoot input {
      width: 100%;
      box-sizing: border-box;
      padding: 3px;
      font-size: 14px;
    }
  </style>


  <br><br><br><br>
</section>
<jsp:include page="includes/lowerpart.jsp" />
