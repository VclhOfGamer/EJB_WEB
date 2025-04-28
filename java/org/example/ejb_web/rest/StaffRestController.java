package org.example.ejb_web.rest;

import org.example.ejb_web.dao.StaffDAO;
import org.example.ejb_web.model.Staff;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

@Path("/staff")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StaffRestController {

    @EJB
    private StaffDAO staffDAO;

    @GET
    public Response getAllStaff() {
        try {
            List<Staff> staffList = staffDAO.findAll();
            return Response.ok(staffList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi lấy danh sách nhân viên: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getStaffById(@PathParam("id") int id) {
        try {
            Staff staff = staffDAO.findById(id);
            if (staff != null) {
                return Response.ok(staff).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy nhân viên với ID: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm nhân viên: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/username/{username}")
    public Response getStaffByUsername(@PathParam("username") String username) {
        try {
            Staff staff = staffDAO.findByUsername(username);
            if (staff != null) {
                return Response.ok(staff).build();
            } else {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy nhân viên với username: " + username)
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm nhân viên: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/search")
    public Response searchStaff(@QueryParam("name") String name, @QueryParam("address") String address) {
        try {
            List<Staff> staffList = staffDAO.filterStaff(name, address);
            return Response.ok(staffList).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi tìm kiếm nhân viên: " + e.getMessage())
                    .build();
        }
    }

    @POST
    public Response addStaff(Staff staff) {
        try {
            if (staff.getName() == null || staff.getUsername() == null ||
                    staff.getPassword() == null || staff.getAddress() == null) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Vui lòng điền đầy đủ thông tin nhân viên")
                        .build();
            }

            Staff existingStaff = staffDAO.findByUsername(staff.getUsername());
            if (existingStaff != null) {
                return Response.status(Status.CONFLICT)
                        .entity("Username đã tồn tại")
                        .build();
            }

            staffDAO.insert(staff);
            return Response.status(Status.CREATED).entity(staff).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi thêm nhân viên: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateStaff(@PathParam("id") int id, Staff staff) {
        try {
            Staff existingStaff = staffDAO.findById(id);
            if (existingStaff == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy nhân viên với ID: " + id)
                        .build();
            }

            staff.setId(id);

            if (staff.getName() == null || staff.getUsername() == null ||
                    staff.getPassword() == null || staff.getAddress() == null) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Vui lòng điền đầy đủ thông tin nhân viên")
                        .build();
            }

            staffDAO.update(staff);
            return Response.ok(staff).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi cập nhật nhân viên: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteStaff(@PathParam("id") int id) {
        try {
            Staff existingStaff = staffDAO.findById(id);
            if (existingStaff == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("Không tìm thấy nhân viên với ID: " + id)
                        .build();
            }

            staffDAO.delete(id);
            return Response.ok("Đã xóa nhân viên thành công").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Lỗi khi xóa nhân viên: " + e.getMessage())
                    .build();
        }
    }

}