<%@page import="model.Product"%>
<%@page import="dao.ProductDAOImpl"%>
<%@page import="dao.ProductDAO"%>
<%@ page import="javax.ejb.*" %>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script type="text/javascript">
    window.onload = function () {
        OpenBootstrapPopup();
    };
    function OpenBootstrapPopup() {
        $("#restockModal").modal('show');
    }
</script>
<% 
ProductDAO pdao = new ProductDAOImpl();
Product product = new Product();
product = pdao.selectProduct(Integer.parseInt(request.getParameter("id")));

%>

<!-- Modal -->
<div class="modal fade" id="restockModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">

                <h5 class="modal-title text-dark" id="exampleModalLabel">Restock Product</h5>

            </div>
            <form action="restockProduct" method="post">
                <div class="modal-body" >
                    <input hidden value="<% out.print(product.getProdId()); %>" name="prodid" type="text">
                    <p>Product ID#: <% out.print(product.getProdId()); %></p>
                    <p>Product Name: <% out.print(product.getProdName()); %></p>
                    <p>Quantity: <input min="0" type="number" value="<% out.print(product.getProdQty()); %>" class="form-control" name="prodQty" placeholder="Quantity"></p>

                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Save</button>
                    <button type="button" class="btn btn-danger" data-mdb-dismiss="modal">Cancel</button>

                </div>
            </form>
        </div>
    </div>
</div>

<style>
    .center-lottie{
        position: absolute;
        top:35%;
        left: 50%;
        transform: translate(-50% , -50%)
    }
</style>