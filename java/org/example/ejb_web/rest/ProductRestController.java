package org.example.ejb_web.rest;

import org.example.ejb_web.dao.ProductDAO;
import org.example.ejb_web.model.Product;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.time.LocalDateTime;
import java.util.List;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestController {

    @EJB
    private ProductDAO productDAO;

    @GET
    public Response getAllProducts() {
        try {
            List<Product> productList = productDAO.findAll();
            return Response.ok(productList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int id) {
        try {
            Product product = productDAO.findById(id);
            if (product != null) {
                return Response.ok(product).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy sản phẩm với ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/category/{categoryId}")
    public Response getProductsByCategory(@PathParam("categoryId") int categoryId) {
        try {
            List<Product> products = productDAO.findByCateGory(categoryId);
            return Response.ok(products).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm sản phẩm theo danh mục: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response searchProducts(@QueryParam("name") String name,
                                   @QueryParam("price") Double price,

                                   @QueryParam("categoryId") Integer categoryId) {
        try {
            List<Product> products = productDAO.search(name,price, categoryId);
            return Response.ok(products).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm kiếm sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response addProduct(Product product) {
        try {
            if (product.getName() == null || product.getUnit() == null ||
                    product.getPrice() <= 0 || product.getCategoryId() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Vui lòng điền đầy đủ thông tin sản phẩm hợp lệ")
                        .build();
            }

            // Thiết lập ngày thêm là hiện tại
            product.setDateAdded(LocalDateTime.now());

            productDAO.insert(product);
            return Response.status(Status.CREATED).entity(product).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi thêm sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int id, Product product) {
        try {
            Product existingProduct = productDAO.findById(id);
            if (existingProduct == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy sản phẩm với ID: " + id)
                        .build();
            }

            product.setId(id);

            if (product.getName() == null || product.getUnit() == null ||
                    product.getPrice() <= 0 || product.getCategoryId() <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Vui lòng điền đầy đủ thông tin sản phẩm hợp lệ")
                        .build();
            }

            // Giữ nguyên ngày thêm ban đầu
            product.setDateAdded(existingProduct.getDateAdded());

            productDAO.update(product);
            return Response.ok(product).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}/quantity")
    public Response updateProductQuantity(@PathParam("id") int id, @QueryParam("quantity") int quantity) {
        try {
            Product existingProduct = productDAO.findById(id);
            if (existingProduct == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy sản phẩm với ID: " + id)
                        .build();
            }

            if (quantity < 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Số lượng không được âm")
                        .build();
            }

            existingProduct.setQuantity(quantity);
            productDAO.update(existingProduct);

            return Response.ok(existingProduct).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật số lượng sản phẩm: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        try {
            Product existingProduct = productDAO.findById(id);
            if (existingProduct == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy sản phẩm với ID: " + id)
                        .build();
            }

            productDAO.delete(id);
            return Response.ok("Đã xóa sản phẩm thành công").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi xóa sản phẩm: " + e.getMessage())
                    .build();
        }
    }
}