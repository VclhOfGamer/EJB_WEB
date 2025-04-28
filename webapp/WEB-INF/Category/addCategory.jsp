<jsp:include page="../includes/upperpart.jsp" />  
<title>Add Category</title>
<!-- Section: Content -->

<section class="animate__animated animate__fadeIn">


<h2>Add Category</h2>
<hr>
<a class="btn btn-primary" href="Category" role="button">< Back</a>
<br><br>
<div class="card border border-success">
    <div class="card-body">
        <form action="addCategory" method="post">
            <br>
            <div class="form-group">
                <label style="font-size:15px;">Category Name :</label>
                <input type="text" value="" class="form-control" required name="catName">
            </div>
            <br>
            <button type="submit" class="btn btn-success">Add</button>
        </form>
    </div>
</div>

</section>

<!-- Section: Content -->
<jsp:include page="../includes/lowerpart.jsp" />  