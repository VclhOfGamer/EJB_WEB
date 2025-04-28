<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.ejb_web.model.Admin" %>
<jsp:include page="includes/upperpart.jsp" />
<title>Administrator</title>

<section class="animate__animated animate__fadeIn">
  <h2>Administrator</h2>
  <hr>

  <a href="addAdmin" class="btn btn-primary" role="button">+ Add Administrator</a>
  <br><br>
  <input type="search" placeholder="Search Administrator..." class="form-control search-input border border-dark" data-table="table-list"/>
  <br>

  <div class="table-responsive">
    <table class="table table-sm table-striped table-hover text-center table-list" style="font-size:15px;">
      <thead class="text-light bg-info">
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Username</th>
        <th>Password</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <%
        List<Admin> admins = (List<Admin>) request.getAttribute("admins");
        if (admins != null) {
          for (Admin admin : admins) {
      %>
      <tr>
        <td><%= admin.getId() %></td>
        <td><%= admin.getName() %></td>
        <td><%= admin.getUsername() %></td>
        <td>••••••••</td>
        <td>
          <a class="btn btn-sm btn-primary" href="editAdmin?id=<%= admin.getId() %>"><i class="fas fa-edit"></i></a>
          <a class="btn btn-sm btn-danger" href="deleteAdmin?id=<%= admin.getId() %>"><i class="fas fa-trash-alt"></i></a>
        </td>
      </tr>
      <%
          }
        }
      %>
      </tbody>
    </table>
  </div>
</section>
<jsp:include page="includes/lowerpart.jsp" />
