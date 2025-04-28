package org.example.ejb_web.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.ejb_web.dao.*;
import org.example.ejb_web.model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Order", urlPatterns = {"/Order", "/viewOrder"})
public class OrderController extends HttpServlet {

    @Inject private OrderDAO orderDAO;
    @Inject private OrdersProductDAO ordersProductDAO;
    @Inject private ProductDAO productDAO;
    @Inject private StaffDAO staffDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        try {
            // Set flag to indicate we're in the admin view, not staff view
            req.setAttribute("isStaff", false);

            if ("/Order".equals(path)) {
                // Load orders list
                List<Order> orders = orderDAO.findAll();
                req.setAttribute("orders", orders);
                req.setAttribute("totalOrders", orderDAO.countAllOrders());
                req.setAttribute("paidSales", orderDAO.totalPaidOrders());
                req.setAttribute("unpaidSales", orderDAO.totalUnPaidOrders());

                // Load staff list for the add modal
                List<Staff> staffList = staffDAO.findAll();
                req.setAttribute("staffList", staffList);

                req.getRequestDispatcher("/WEB-INF/AllOrder.jsp").forward(req, resp);
            }

            else if ("/viewOrder".equals(path)) {
                String orderIdParam = req.getParameter("orderid");
                String action = req.getParameter("action");

                if ("deleteProduct".equals(action)) {
                    int orderId = Integer.parseInt(req.getParameter("orderId"));
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    ordersProductDAO.deleteProductFromOrder(orderId, productId);
                    resp.sendRedirect("viewOrder?orderid=" + orderId);
                    return;
                }

                if ("confirmOrder".equals(action) && orderIdParam != null) {
                    int orderId = Integer.parseInt(orderIdParam);
                    orderDAO.updateStatus(orderId, "PAID");
                    resp.sendRedirect("viewOrder?orderid=" + orderId);
                    return;
                }

                // Load order details page
                if (orderIdParam != null) {
                    int orderId = Integer.parseInt(orderIdParam);

                    Order order = orderDAO.findById(orderId);
                    List<OrdersProduct> orderItems = ordersProductDAO.findByOrderId(orderId);
                    List<Product> productList = productDAO.findAll();
                    Staff staff = staffDAO.findById(order.getStaffId());

                    // Calculate total price
                    double total = 0;
                    for (OrdersProduct op : orderItems) {
                        Product product = productDAO.findById(op.getProductId());
                        total += product.getPrice() * op.getQuantity();
                    }

                    double profit = 0;
                    if ("DELIVER".equalsIgnoreCase(order.getOrderType())) {
                        profit = total * 0.2;
                    }

                    req.setAttribute("order", order);
                    req.setAttribute("orderItems", orderItems);
                    req.setAttribute("productList", productList);
                    req.setAttribute("staff", staff);
                    req.setAttribute("totalPrice", total);
                    req.setAttribute("profit", profit);

                    // Show modal if needed
                    if (action != null && !"confirmOrder".equals(action)) {
                        req.setAttribute("modalAction", action);
                        if ("edit".equals(action)) {
                            int opid = Integer.parseInt(req.getParameter("opid"));
                            OrdersProduct editItem = ordersProductDAO.findById(opid);
                            req.setAttribute("editItem", editItem);
                        }
                    }

                    // Reuse the same view JSP as staff but with admin controls
                    req.getRequestDispatcher("/WEB-INF/Order/viewOrder.jsp").forward(req, resp);
                } else {
                    resp.sendRedirect("Order");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String action = req.getParameter("action");

        try {
            if (action == null) {
                resp.sendRedirect("error.jsp");
                return;
            }

            switch (action) {
                case "addOrder": {
                    // For admin view, get staff ID from form
                    int staffId = Integer.parseInt(req.getParameter("staffId"));
                    String orderType = req.getParameter("orderType");

                    Order newOrder = new Order();
                    newOrder.setStaffId(staffId);
                    newOrder.setOrderType(orderType);
                    newOrder.setStatus("UNPAID");
                    newOrder.setOrderDate(java.time.LocalDateTime.now());

                    int orderId = orderDAO.insert(newOrder);

                    resp.sendRedirect("viewOrder?orderid=" + orderId);
                    break;
                }

                case "addProduct": {
                    int orderId = Integer.parseInt(req.getParameter("orderId"));
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    int quantity = Integer.parseInt(req.getParameter("quantity"));

                    // Check if we're adding to a DELIVER order and verify stock
                    Order order = orderDAO.findById(orderId);
                    Product product = productDAO.findById(productId);

                    if ("DELIVER".equals(order.getOrderType()) && quantity > product.getQuantity()) {
                        // Not enough stock
                        resp.sendRedirect("viewOrder?orderid=" + orderId + "&error=insufficient_stock");
                        return;
                    }

                    if (ordersProductDAO.existsInOrder(orderId, productId)) {
                        ordersProductDAO.updateQuantity(orderId, productId, quantity);
                    } else {
                        OrdersProduct op = new OrdersProduct();
                        op.setOrderId(orderId);
                        op.setProductId(productId);
                        op.setQuantity(quantity);
                        ordersProductDAO.insert(op);
                    }
                    resp.sendRedirect("viewOrder?orderid=" + orderId);
                    break;
                }

                case "editProduct": {
                    int orderId = Integer.parseInt(req.getParameter("orderId"));
                    int productId = Integer.parseInt(req.getParameter("productId"));
                    int quantity = Integer.parseInt(req.getParameter("quantity"));

                    // Check stock if it's a DELIVER order
                    Order order = orderDAO.findById(orderId);
                    if ("DELIVER".equals(order.getOrderType()) && quantity > 0) {
                        Product product = productDAO.findById(productId);
                        if (quantity > product.getQuantity()) {
                            // Not enough stock
                            resp.sendRedirect("viewOrder?orderid=" + orderId + "&error=insufficient_stock");
                            return;
                        }
                    }

                    if (quantity == 0) {
                        ordersProductDAO.deleteProductFromOrder(orderId, productId);
                    } else {
                        ordersProductDAO.updateQuantity(orderId, productId, quantity);
                    }

                    resp.sendRedirect("viewOrder?orderid=" + orderId);
                    break;
                }

                case "confirmOrder": {
                    int orderId = Integer.parseInt(req.getParameter("orderid"));
                    Order order = orderDAO.findById(orderId);

                    if (order != null) {
                        orderDAO.updateStatus(orderId, "PAID");

                        List<OrdersProduct> orderItems = ordersProductDAO.findByOrderId(orderId);

                        for (OrdersProduct item : orderItems) {
                            Product product = productDAO.findById(item.getProductId());
                            int currentQuantity = product.getQuantity();
                            int orderQuantity = item.getQuantity();

                            if ("DELIVER".equals(order.getOrderType())) {
                                // For deliveries, decrease stock
                                if (orderQuantity > currentQuantity) {
                                    // Not enough stock
                                    resp.sendRedirect("viewOrder?orderid=" + orderId + "&error=insufficient_stock");
                                    return;
                                }
                                productDAO.updateQuantity(item.getProductId(), currentQuantity - orderQuantity);
                            }
                            else if ("RECEIVE".equals(order.getOrderType())) {
                                // For receiving, increase stock
                                productDAO.updateQuantity(item.getProductId(), currentQuantity + orderQuantity);

                                // Update date_added and added_by
                                Staff staff = staffDAO.findById(order.getStaffId());
                                java.sql.Date sqlDate = java.sql.Date.valueOf(order.getOrderDate().toLocalDate());
                                productDAO.updateProductInfo(
                                        item.getProductId(),
                                        sqlDate,
                                        staff.getName()
                                );
                            }
                        }
                    }

                    resp.sendRedirect("viewOrder?orderid=" + orderId);
                    break;
                }

                default: {
                    resp.sendRedirect("Order");
                    break;
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("error", "Invalid number format.");
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong: " + e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }
}