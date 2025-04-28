<jsp:include page="../includes/upperpart.jsp" />
<title>Add Staff</title>

<section class="animate__animated animate__fadeIn">
    <h2>Add Staff</h2>
    <hr>
    <a class="btn btn-primary" href="Staff" role="button">&lt; Back</a>
    <br><br>

    <div class="card border border-success">
        <div class="card-body">
            <form action="addStaff" method="post">
                <div class="form-group">
                    <label>Full Name:</label>
                    <input required type="text" name="name" class="form-control">
                </div>
                <div class="form-group">
                    <label>Username:</label>
                    <input required type="text" name="username" class="form-control">
                </div>
                <div class="form-group">
                    <label>Password:</label>
                    <input required type="password" name="password" class="form-control">
                </div>
                <div class="form-group">
                    <label>Address:</label>
                    <input required type="text" name="address" class="form-control">
                </div>
                <br>
                <button type="submit" class="btn btn-success">Add</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../includes/lowerpart.jsp" />
