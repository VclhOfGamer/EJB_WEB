package org.example.ejb_web.rest;

import org.example.ejb_web.dao.OrderDAO;
import org.example.ejb_web.model.Order;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderRestController {

    @EJB
    private OrderDAO orderDAO;

    @GET
    public Response getAllOrders() {
        try {
            List<Order> orderList = orderDAO.findAll();
            return Response.ok(orderList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy danh sách đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") int id) {
        try {
            Order order = orderDAO.findById(id);
            if (order != null) {
                return Response.ok(order).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy đơn hàng với ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response addOrder(Order order) {
        try {
            orderDAO.insert(order);
            return Response.status(Status.CREATED).entity(order).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi thêm đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") int id, Order order) {
        try {
            Order existingOrder = orderDAO.findById(id);
            if (existingOrder == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy đơn hàng với ID: " + id)
                        .build();
            }

            order.setId(id);
            orderDAO.update(order);
            return Response.ok(order).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") int id) {
        try {
            Order existingOrder = orderDAO.findById(id);
            if (existingOrder == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy đơn hàng với ID: " + id)
                        .build();
            }

            orderDAO.delete(id);
            return Response.ok("Đã xóa đơn hàng thành công").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi xóa đơn hàng: " + e.getMessage())
                    .build();
        }
    }
}