<jsp:include page="../includes/upperpart.jsp" />
<title>Add Administrator</title>

<section class="animate__animated animate__fadeIn">
    <h2>Add Administrator</h2>
    <hr>
    <a class="btn btn-primary" href="Admin" role="button">&lt; Back</a>
    <br><br>

    <div class="card border">
        <div class="card-body">
            <form action="addAdmin" method="post">
                <p>Name:<input class="form-control" type="text" name="name" required /></p>
                <p>Username:<input class="form-control" type="text" name="username" required /></p>
                <p>Password:<input class="form-control" type="password" name="password" required /></p>
                <button type="submit" class="btn btn-success">Add</button>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../includes/lowerpart.jsp" />
