<jsp:include page="../includes/upperpart.jsp" />
<title>Delete Staff</title>

<section class="animate__animated animate__fadeIn">
    <h2>Delete Staff</h2>
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

    <div class="card border border-danger">
        <div class="card-body">
            <div class="alert alert-danger">
                Are you sure to delete this staff?
            </div>
            <form action="deleteStaff" method="post">
                <input type="hidden" name="id" value="<%= staff.getId() %>">
                <p>Name: <strong><%= staff.getName() %></strong></p>
                <p>Username: <strong><%= staff.getUsername() %></strong></p>
                <p>Address: <strong><%= staff.getAddress() %></strong></p>
                <br>
                <button type="submit" class="btn btn-danger">Delete</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../includes/lowerpart.jsp" />
