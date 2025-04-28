            <nav id="main-navbar" class="navbar navbar-expand-lg text-light navbar-dark bg-danger fixed-top">
                <!-- Container wrapper -->
                <div class="container-fluid">
                    <!-- Toggle button -->
                    <button class="navbar-toggler" type="button" data-mdb-toggle="collapse" data-mdb-target="#sidebarMenu"
                            aria-controls="sidebarMenu" aria-expanded="false" aria-label="Toggle navigation">
                        <i class="fas fa-bars"></i>
                    </button>

                    <!-- Brand -->
                    <a class="navbar-brand" href="AdminDashboard">
                        <img src="${pageContext.request.contextPath}/img/logo.png" height="25" alt="" loading="lazy" />
                        Inventory Management System
                    </a>

                    <!-- Right links -->
                    <ul class="navbar-nav ms-auto d-flex flex-row">

                        <!-- Icon -->
                        <li  class="nav-item me-3 me-lg-0">
                            <a class="nav-link" href="logoutAdmin">
                                <i class="fas fa-sign-out-alt"></i>
                                <small>Logout</small>
                            </a>
                        </li>

                    </ul>
                </div>
                <!-- Container wrapper -->
            </nav>