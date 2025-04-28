<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<jsp:include page="includes/upperpart.jsp" />
<jsp:include page="includes/cardbg.jsp" />

<title>Admin Dashboard</title>

<!-- Section: Content -->
<section class="animate__animated animate__fadeIn">

    <h2 class="animate__animated animate__fadeInDown">Dashboard</h2>
    <hr>

    <div class="row text-light animate__animated animate__fadeInDown">
    <div class="col" style="color: black">
            <div class="card border text-center bg-c-pink">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-box fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("totalInventory")) %></label>
                            <p class="mb-0">Inventory Value</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Receive card -->
        <div class="col" style="color: black">
            <div class="card border text-center bg-c-orange">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-warehouse fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("totalReceive")) %></label>
                            <p class="mb-0">Total Pays </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Orders Value card -->
        <div class="col" style="color: black">
            <div class="card border text-center bg-c-pink">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-shopping-cart fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("totalDeliver")) %></label>
                            <p class="mb-0">Total Sales </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col" style="color: black">
            <div class="card border text-center bg-c-orange">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-dollar-sign fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("totalProfit")) %></label>
                            <p class="mb-0">Total Profit (20%)</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </div>
    <hr>
    <h2 class="animate__animated animate__fadeInDown">Total in this month</h2>
    <hr>

    <div class="row text-light animate__animated animate__fadeInDown">
        <div class="col" style="color: black">
            <div class="card border text-center bg-c-pink">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-shopping-bag fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;"><%= request.getAttribute("monthlyOrderCount") %></label>
                            <p class="mb-0">Monthly Orders</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Monthly Receive card -->
        <div class="col" style="color: black">
            <div class="card border text-center bg-c-orange">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-warehouse fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("monthlyReceive")) %></label>
                            <p class="mb-0">Monthly Pays</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Monthly Sales card -->
        <div class="col" style="color: black">
            <div class="card border text-center bg-c-pink">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-shopping-cart fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("monthlyDelivery")) %></label>
                            <p class="mb-0">Monthly Sales</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Monthly Profit card -->
        <div class="col" style="color: black">
            <div class="card border text-center bg-c-orange">
                <div class="card-body">
                    <div class="d-flex justify-content-between px-md-1">
                        <div class="align-self-center">
                            <i class="fas fa-dollar-sign fa-3x"></i>
                        </div>
                        <div class="text-end">
                            <label style="font-size:25px;">$ <%= String.format("%,.2f", request.getAttribute("monthlyProfit")) %></label>
                            <p class="mb-0">Monthly Profit (20%)</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <br>

    <%
        Map<String, Double> dailyOrders = (Map<String, Double>) request.getAttribute("dailyOrders");
        if (dailyOrders == null) {
            dailyOrders = new HashMap<>();
        }
    %>


    <div class="row">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
        <canvas class="animate__animated animate__fadeInUp" id="bar-chart" style="width:300px; height:100px;"></canvas>
        <script>
            const labels = [
                <% java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM"); %>
                <% for (String date : dailyOrders.keySet()) {
                       java.time.LocalDate d = java.time.LocalDate.parse(date);
                       String label = d.format(formatter); %>
                '<%= label %>',
                <% } %>
            ];

            const data = [
                <% for (Double value : dailyOrders.values()) { %>
                <%= String.format(Locale.US, "%.2f", value) %>,
                <% } %>
            ];


            new Chart(document.getElementById("bar-chart"), {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: "Sales by Date ($)",
                        backgroundColor: ["#3e95cd", "#8e5ea2", "#3cba9f", "#e8c3b9", "#c45850", "#f39c12", "#1abc9c"],
                        data: data
                    }]
                },
                options: {
                    legend: { display: true },
                    title: {
                        display: true,
                        text: 'Total Sales for last 7 Days ($)'
                    }
                }
            });
        </script>
    </div>
    <br>

</section>
<jsp:include page="includes/lowerpart.jsp" />
