<jsp:include page="../includes/upperpart.jsp" />
<title>Edit Staff</title>

<section class="animate__animated animate__fadeIn">
    <h2>Edit Staff</h2>
    <hr>
    <a class="btn btn-primary" href="Staff" role="button">&lt; Back</a>
    <br><br>

    <%
        org.example.ejb_web.model.Staff staff = (org.example.ejb_web.model.Staff) request.getAttribute("staff");
        if (staff == null) {
    %>
    <div class="alert alert-danger">Staff not found.</div>
    <%
            return;
        }
    %>

    <div class="card border border-success">
        <div class="card-body">
            <form action="editStaff" method="post">
                <input type="hidden" name="id" value="<%= staff.getId() %>">
                <div class="form-group">
                    <label>Name:</label>
                    <input required type="text" name="name" value="<%= staff.getName() %>" class="form-control">
                </div>
                <div class="form-group">
                    <label>Username:</label>
                    <input required type="text" name="username" value="<%= staff.getUsername() %>" class="form-control">
                </div>
                <div class="form-group">
                    <label>Password:</label>
                    <input required type="password" name="password" value="<%= staff.getPassword() %>" class="form-control">
                </div>
                <div class="form-group">
                    <label>Address:</label>
                    <input required type="text" name="address" value="<%= staff.getAddress() %>" class="form-control">
                </div>
                <br>
                <button type="submit" class="btn btn-success">Save</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../includes/lowerpart.jsp" />
