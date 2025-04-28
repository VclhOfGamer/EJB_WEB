package org.example.ejb_web.rest;

import org.example.ejb_web.dao.CategoryDAO;
import org.example.ejb_web.model.Category;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryRestController {

    @EJB
    private CategoryDAO categoryDAO;

    @GET
    public Response getAllCategories() {
        try {
            List<Category> categoryList = categoryDAO.findAll();
            return Response.ok(categoryList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy danh sách danh mục: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") int id) {
        try {
            Category category = categoryDAO.findById(id);
            if (category != null) {
                return Response.ok(category).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy danh mục với ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm danh mục: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response addCategory(Category category) {
        try {
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Tên danh mục không được để trống")
                        .build();
            }

            categoryDAO.insertCategory(category);
            return Response.status(Status.CREATED).entity(category).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi thêm danh mục: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") int id, Category category) {
        try {
            Category existingCategory = categoryDAO.findById(id);
            if (existingCategory == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy danh mục với ID: " + id)
                        .build();
            }

            category.setId(id);

            if (category.getName() == null || category.getName().trim().isEmpty()) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Tên danh mục không được để trống")
                        .build();
            }

            categoryDAO.updateCategory(category);
            return Response.ok(category).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật danh mục: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") int id) {
        try {
            Category existingCategory = categoryDAO.findById(id);
            if (existingCategory == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy danh mục với ID: " + id)
                        .build();
            }

            categoryDAO.deleteCategory(id);
            return Response.ok("Đã xóa danh mục thành công").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi xóa danh mục: " + e.getMessage())
                    .build();
        }
    }
}