package org.example.ejb_web.controller;

import org.example.ejb_web.dao.StaffDAO;
import org.example.ejb_web.model.Staff;

import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Staff", urlPatterns = {
        "/Staff", "/addStaff", "/editStaff", "/deleteStaff", "/StaffProfile", "/editProfileStaff"
})
public class StaffController extends HttpServlet {

    @EJB
    private StaffDAO staffDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        RequestDispatcher dispatcher = null;

        switch (userPath) {
            case "/addStaff":
                dispatcher = request.getRequestDispatcher("/WEB-INF/Staff/addStaff.jsp");
                break;

            case "/editStaff":
                String editIdStr = request.getParameter("id");
                if (editIdStr != null) {
                    int editId = Integer.parseInt(editIdStr);
                    Staff editStaff = staffDAO.findById(editId);
                    request.setAttribute("staff", editStaff);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/Staff/editStaff.jsp");
                break;

            case "/deleteStaff":
                String delIdStr = request.getParameter("id");
                if (delIdStr != null) {
                    int delId = Integer.parseInt(delIdStr);
                    Staff delStaff = staffDAO.findById(delId);
                    request.setAttribute("staff", delStaff);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/Staff/deleteStaff.jsp");
                break;

            case "/StaffProfile":
                // Get staff ID from session
                HttpSession session = request.getSession();
                Integer staffId = (Integer) session.getAttribute("id");

                if (staffId != null) {
                    // Fetch staff data from database
                    Staff profileStaff = staffDAO.findById(staffId);
                    // Set staff object in request for JSP
                    request.setAttribute("staff", profileStaff);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/StaffView/StaffProfile.jsp");
                break;

            default: // /Staff
                String name = request.getParameter("name");
                String address = request.getParameter("address");
                String sortBy = request.getParameter("sortBy");
                String sortDir = request.getParameter("sortDir");

                List<Staff> staffList;

                if ((name != null && !name.isEmpty()) || (address != null && !address.isEmpty())) {
                    staffList = staffDAO.filterStaff(name, address);
                } else if (sortBy != null && !sortBy.isEmpty()) {
                    boolean ascending = !"desc".equalsIgnoreCase(sortDir);
                    staffList = staffDAO.sortStaff(sortBy, ascending);
                } else {
                    staffList = staffDAO.findAll();
                }

                request.setAttribute("staffList", staffList);
                dispatcher = request.getRequestDispatcher("/WEB-INF/AllStaff.jsp");
                break;
        }

        dispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        Staff staff = new Staff();

        switch (userPath) {
            case "/addStaff":
                staff.setName(request.getParameter("name"));
                staff.setUsername(request.getParameter("username"));
                staff.setPassword(request.getParameter("password"));
                staff.setAddress(request.getParameter("address"));
                staffDAO.insert(staff);
                break;

            case "/editStaff":
                staff.setId(Integer.parseInt(request.getParameter("id")));
                staff.setName(request.getParameter("name"));
                staff.setUsername(request.getParameter("username"));
                staff.setPassword(request.getParameter("password"));
                staff.setAddress(request.getParameter("address"));
                staffDAO.update(staff);
                break;

            case "/deleteStaff":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                staffDAO.delete(deleteId);
                break;

            case "/editProfileStaff":
                HttpSession session = request.getSession();
                Integer staffId = (Integer) session.getAttribute("id");

                if (staffId != null) {
                    staff.setId(staffId);
                    staff.setName(request.getParameter("name"));
                    staff.setUsername((String) session.getAttribute("username"));
                    staff.setPassword(request.getParameter("password"));
                    staff.setAddress(request.getParameter("address"));
                    staffDAO.update(staff);
                    response.sendRedirect("StaffProfile?code=editSuccess");
                    return;
                } else {
                    response.sendRedirect("login"); // Redirect to login if session is invalid
                    return;
                }
        }

        // Default action for other paths - show staff list
        List<Staff> staffList = staffDAO.findAll();
        request.setAttribute("staffList", staffList);
        request.getRequestDispatcher("/WEB-INF/AllStaff.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Quản lý các thao tác CRUD nhân viên, có hỗ trợ lọc và sắp xếp.";
    }
}