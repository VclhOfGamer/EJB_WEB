package org.example.ejb_web.rest;

import org.example.ejb_web.dao.OrdersProductDAO;
import org.example.ejb_web.model.OrdersProduct;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

@Path("/ordersproduct")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdersProductRestController {

    @EJB
    private OrdersProductDAO ordersProductDAO;

    @GET
    public Response getAllOrdersProducts() {
        try {
            List<OrdersProduct> ordersProductList = ordersProductDAO.findAll();
            return Response.ok(ordersProductList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy danh sách chi tiết đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrdersProductById(@PathParam("id") int id) {
        try {
            OrdersProduct ordersProduct = ordersProductDAO.findById(id);
            if (ordersProduct != null) {
                return Response.ok(ordersProduct).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy chi tiết đơn hàng với ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm chi tiết đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/order/{orderId}")
    public Response getOrdersProductsByOrderId(@PathParam("orderId") int orderId) {
        try {
            List<OrdersProduct> ordersProductList = ordersProductDAO.findByOrderId(orderId);
            return Response.ok(ordersProductList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy chi tiết đơn hàng theo mã đơn: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/product/{productId}")
    public Response getOrdersProductsByProductId(@PathParam("productId") int productId) {
        try {
            List<OrdersProduct> ordersProductList = ordersProductDAO.findByProductId(productId);
            return Response.ok(ordersProductList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy chi tiết đơn hàng theo mã sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response addOrdersProduct(OrdersProduct ordersProduct) {
        try {
            if (ordersProduct.getOrderId() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Mã đơn hàng không hợp lệ")
                        .build();
            }

            if (ordersProduct.getProductId() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Mã sản phẩm không hợp lệ")
                        .build();
            }

            if (ordersProduct.getQuantity() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Số lượng phải lớn hơn 0")
                        .build();
            }

            ordersProductDAO.insert(ordersProduct);
            return Response.status(Status.CREATED).entity(ordersProduct).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateOrdersProduct(@PathParam("id") int id, OrdersProduct ordersProduct) {
        try {
            OrdersProduct existingOrdersProduct = ordersProductDAO.findById(id);
            if (existingOrdersProduct == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy chi tiết đơn hàng với ID: " + id)
                        .build();
            }

            ordersProduct.setId(id);

            if (ordersProduct.getOrderId() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Mã đơn hàng không hợp lệ")
                        .build();
            }

            if (ordersProduct.getProductId() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Mã sản phẩm không hợp lệ")
                        .build();
            }

            if (ordersProduct.getQuantity() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Số lượng phải lớn hơn 0")
                        .build();
            }

            ordersProductDAO.update(ordersProduct);
            return Response.ok(ordersProduct).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật chi tiết đơn hàng: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrdersProduct(@PathParam("id") int id) {
        try {
            OrdersProduct existingOrdersProduct = ordersProductDAO.findById(id);
            if (existingOrdersProduct == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy chi tiết đơn hàng với ID: " + id)
                        .build();
            }

            ordersProductDAO.delete(id);
            return Response.ok("Đã xóa chi tiết đơn hàng thành công").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi xóa chi tiết đơn hàng: " + e.getMessage())
                    .build();
        }
    }
}