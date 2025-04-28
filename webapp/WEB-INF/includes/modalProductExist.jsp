<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script type="text/javascript">
    window.onload = function () {
        OpenBootstrapPopup();
    };
    function OpenBootstrapPopup() {
        $("#existModal").modal('show');
    }
</script>

<!-- Modal -->
<div class="modal fade" id="existModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div  class="modal-header bg-danger">
                <h5 class="modal-title text-light" id="exampleModalLabel">Alert</h5>

            </div>
            <div class="modal-body" style="height:150px;">
                <div class="center-lottie">
                    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
                    <lottie-player src="https://assets4.lottiefiles.com/packages/lf20_qpwbiyxf.json"  background="transparent"  speed="0.6"  style="width: 100px; height: 100px;"    autoplay></lottie-player>
                </div>
                <div class="text-center" style="margin-top:100px;">
                    Product already exist !
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