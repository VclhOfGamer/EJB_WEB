<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.ejb_web.model.Staff" %>
<jsp:include page="includes/upperpart.jsp" />
<title>Staff</title>

<section class="animate__animated animate__fadeIn">
  <h2>Staff</h2>
  <hr>
  <a href="addStaff" class="btn btn-primary">+ Add Staff</a>
  <br><br>
  <input type="search" placeholder="Search Staff..." class="form-control search-input border border-dark" data-table="table-list"/>
  <br>
  <div class="table-responsive">
    <table class="table table-sm table-striped table-hover text-center table-list">
      <thead class="text-light bg-info">
      <tr>
        <th>ID</th>
        <th>Full Name</th>
        <th>Username</th>
        <th>Address</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <%
        List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
        for (Staff staff : staffList) {
      %>
      <tr>
        <td><%= staff.getId() %></td>
        <td><%= staff.getName() %></td>
        <td><%= staff.getUsername() %></td>
        <td><%= staff.getAddress() %></td>
        <td>
          <a class="btn btn-sm btn-primary" href="editStaff?id=<%= staff.getId() %>"><i class="fas fa-edit"></i></a>
          <a class="btn btn-sm btn-danger" href="deleteStaff?id=<%= staff.getId() %>"><i class="fas fa-trash-alt"></i></a>
        </td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>
</section>
<jsp:include page="includes/lowerpart.jsp" />
