package org.example.ejb_web.rest;

import org.example.ejb_web.dao.AdminDAO;
import org.example.ejb_web.model.Admin;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminRestController {

    @EJB
    private AdminDAO adminDAO;

    @GET
    public Response getAllAdmins() {
        try {
            List<Admin> adminList = (List<Admin>) adminDAO.selectAllAdmin();
            return Response.ok(adminList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy danh sách quản trị viên: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getAdminById(@PathParam("id") int id) {
        try {
            Admin admin = adminDAO.findById(id);
            if (admin != null) {
                return Response.ok(admin).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy quản trị viên với ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm quản trị viên: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/username/{username}")
    public Response getAdminByUsername(@PathParam("username") String username) {
        try {
            Admin admin = adminDAO.findByUsername(username);
            if (admin != null) {
                return Response.ok(admin).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy quản trị viên với username: " + username)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm quản trị viên: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response addAdmin(Admin admin) {
        try {
            if (admin.getName() == null || admin.getUsername() == null || admin.getPassword() == null) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Vui lòng điền đầy đủ thông tin quản trị viên")
                        .build();
            }

            Admin existingAdmin = adminDAO.findByUsername(admin.getUsername());
            if (existingAdmin != null) {
                return Response.status(Status.CONFLICT)
                        .entity("Username đã tồn tại")
                        .build();
            }

            adminDAO.insertAdmin(admin);
            return Response.status(Status.CREATED).entity(admin).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi thêm quản trị viên: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateAdmin(@PathParam("id") int id, Admin admin) {
        try {
            Admin existingAdmin = adminDAO.findById(id);
            if (existingAdmin == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy quản trị viên với ID: " + id)
                        .build();
            }

            admin.setId(id);

            if (admin.getName() == null || admin.getUsername() == null || admin.getPassword() == null) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Vui lòng điền đầy đủ thông tin quản trị viên")
                        .build();
            }

            adminDAO.updateAdmin(admin);
            return Response.ok(admin).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật quản trị viên: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAdmin(@PathParam("id") int id) {
        try {
            Admin existingAdmin = adminDAO.findById(id);
            if (existingAdmin == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy quản trị viên với ID: " + id)
                        .build();
            }

            adminDAO.deleteAdmin(id);
            return Response.ok("Đã xóa quản trị viên thành công").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi xóa quản trị viên: " + e.getMessage())
                    .build();
        }
    }



}