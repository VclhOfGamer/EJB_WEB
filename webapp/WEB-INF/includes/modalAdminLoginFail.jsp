<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script type="text/javascript">
    window.onload = function () {
        OpenBootstrapPopup();
    };
    function OpenBootstrapPopup() {
        $("#adminloginfailModal").modal('show');
    }
</script>

<!-- Modal -->
<div class="modal fade" id="adminloginfailModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div  class="modal-header bg-danger">
                <h5 class="modal-title text-light" id="exampleModalLabel"> Error </h5>

            </div>
            <div class="modal-body" style="height:180px;">
                <div class="center-lottie">
                    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
                    <lottie-player src="https://assets10.lottiefiles.com/packages/lf20_ddxv3rxw.json"  background="transparent"  speed="2"  style="width: 80px; height: 80px;" loop   autoplay></lottie-player>
                </div>
                <div class="text-center" style="margin-top:100px;">
                    <p class="lead">Sai ten dang nhap hoac mat khau !</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-mdb-dismiss="modal">OK</button>
            </div>
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