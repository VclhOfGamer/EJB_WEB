    package org.example.ejb_web.controller;

    import jakarta.inject.Inject;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.*;
    import org.example.ejb_web.dao.OrderDAO;
    import org.example.ejb_web.dao.OrdersProductDAO;
    import org.example.ejb_web.dao.ProductDAO;

    import java.io.IOException;
    import java.sql.Date;
    import java.time.LocalDate;
    import java.util.LinkedHashMap;
    import java.util.Map;

    @WebServlet(name = "Dashboard", urlPatterns = {"/AdminDashboard", "/StaffDashboard"})
    public class DashboardController extends HttpServlet {

        @Inject
        private OrderDAO orderDAO;

        @Inject
        private OrdersProductDAO ordersProductDAO;

        @Inject
        private ProductDAO productDAO;

        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            response.setContentType("text/html;charset=UTF-8");

            try {
                // Tổng quan
                double totalInventory = productDAO.getTotalInventory();
                double totalDeliver = ordersProductDAO.getTotalOrdersByTypeAndStatus("DELIVER", "PAID");
                double totalReceive = ordersProductDAO.getTotalOrdersByTypeAndStatus("RECEIVE", "PAID");

                double totalProfit = totalDeliver * 0.2;

                LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
                LocalDate currentDate = LocalDate.now();

                int monthlyOrderCount = orderDAO.getOrderCountByDateRange(
                        firstDayOfMonth.toString(),
                        currentDate.toString(),
                        "DELIVER",
                        "PAID");


                double monthlyReceive = ordersProductDAO.getTotalOrdersByDateRangeAndTypeStatus(
                        firstDayOfMonth.toString(),
                        currentDate.toString(),
                        "RECEIVE",
                        "PAID");

                double monthlyDelivery = ordersProductDAO.getTotalOrdersByDateRangeAndTypeStatus(
                        firstDayOfMonth.toString(),
                        currentDate.toString(),
                        "DELIVER",
                        "PAID");

                double monthlyProfit = monthlyDelivery * 0.2;


                request.setAttribute("monthlyOrderCount", monthlyOrderCount);
                request.setAttribute("monthlyReceive", monthlyReceive);
                request.setAttribute("monthlyDelivery", monthlyDelivery);
                request.setAttribute("monthlyProfit", monthlyProfit);

                // Dữ liệu biểu đồ 7 ngày gần nhất
                Map<String, Double> dailyOrders = new LinkedHashMap<>();
                Map<String, Double> dailyProfits = new LinkedHashMap<>();

                for (int i = 6; i >= 0; i--) {
                    LocalDate date = LocalDate.now().minusDays(i);
                    String label = date.toString();
                    double sales = ordersProductDAO.getTotalOrdersByDateAndTypeStatus(label, "DELIVER", "PAID");
                    dailyOrders.put(label, sales);
                }



                // Gửi dữ liệu đến giao diện
                request.setAttribute("totalInventory", totalInventory);
                request.setAttribute("totalDeliver", totalDeliver);
                request.setAttribute("totalReceive", totalReceive);
                request.setAttribute("totalProfit", totalProfit);
                request.setAttribute("dailyOrders", dailyOrders);
                request.setAttribute("dailyProfits", dailyProfits);

                // Điều hướng đến dashboard phù hợp
                String userPath = request.getServletPath();
                HttpSession session = request.getSession(false);
                if (userPath.equals("/StaffDashboard")) {
                    if (session == null || session.getAttribute("staffLogin") == null) {
                        System.out.println("Session staff invalide, redirection StaffLogin");
                        request.getRequestDispatcher("/WEB-INF/StaffView/StaffDashboard.jsp").forward(request, response);
                        return;
                    }
                    System.out.println("Session staff  " + session.getAttribute("name"));
                } else {
                    if (session == null || session.getAttribute("adminLogin") == null) {
                        System.out.println("Session admin invalide, redirection AdminLogin");
                        request.getRequestDispatcher("/WEB-INF/AdminDashboard.jsp").forward(request, response);
                        return;
                    }
                    System.out.println("Session admin  " + session.getAttribute("name"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Không thể tải dashboard: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
            }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            processRequest(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            processRequest(request, response);
        }

        @Override
        public String getServletInfo() {
            return "Dashboard";
        }
    }
