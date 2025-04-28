package org.example.ejb_web.controller;

import org.example.ejb_web.dao.AdminDAO;
import org.example.ejb_web.model.Admin;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Admin", urlPatterns = {"/Admin", "/addAdmin", "/editAdmin", "/deleteAdmin"})
public class AdminController extends HttpServlet {

    @EJB
    private AdminDAO adao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String userPath = request.getServletPath();
        RequestDispatcher dispatcher;

        switch (userPath) {
            case "/addAdmin":
                dispatcher = request.getRequestDispatcher("/WEB-INF/Admin/addAdmin.jsp");
                break;
            case "/editAdmin":
                String editIdStr = request.getParameter("id");
                if (editIdStr != null) {
                    int editId = Integer.parseInt(editIdStr);
                    Admin editAdmin = adao.findById(editId);
                    request.setAttribute("admin", editAdmin);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/Admin/editAdmin.jsp");
                break;

            case "/deleteAdmin":
                String delIdStr = request.getParameter("id");
                if (delIdStr != null) {
                    int delId = Integer.parseInt(delIdStr);
                    Admin delAdmin = adao.findById(delId);
                    request.setAttribute("admin", delAdmin);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/Admin/deleteAdmin.jsp");
                break;

            default:
                dispatcher = request.getRequestDispatcher("/WEB-INF/AllAdmin.jsp");
                break;
        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();

        if ("/Admin".equals(userPath)) {
            // Lấy tham số filter và sort từ request
            String usernameKeyword = request.getParameter("username");
            String nameKeyword = request.getParameter("name");
            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("order"); // asc | desc

            List<Admin> admins;

            boolean hasFilter = (usernameKeyword != null && !usernameKeyword.isEmpty()) ||
                    (nameKeyword != null && !nameKeyword.isEmpty());

            boolean hasSort = (sortBy != null && !sortBy.isEmpty());

            // Xử lý tìm kiếm & lọc
            if (hasFilter && !hasSort) {
                admins = adao.filterAdmins(usernameKeyword, nameKeyword);
            }
            // Xử lý sắp xếp
            else if (hasSort) {
                boolean ascending = !"desc".equalsIgnoreCase(sortOrder);
                admins = adao.sortAdmins(sortBy, ascending);
            }
            // Không lọc, không sắp xếp
            else {
                admins = adao.selectAllAdmin();
            }

            // Lấy thông tin admin đang đăng nhập
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("adminid") != null) {
                int adminId = (int) session.getAttribute("adminid");
                Admin admin = adao.findById(adminId);
                String adminDisplay = admin.getName();
                request.setAttribute("adminDisplay", adminDisplay);
            }

            // Gửi danh sách về giao diện
            request.setAttribute("admins", admins);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/AllAdmin.jsp");
            dispatcher.forward(request, response);

        } else {
            processRequest(request, response);
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        Admin admin = new Admin();

        switch (userPath) {
            case "/addAdmin":
                populateAdminFromRequest(request, admin);
                adao.insertAdmin(admin);
                break;

            case "/editAdmin":
                populateAdminFromRequest(request, admin);
                adao.updateAdmin(admin);
                break;

            case "/deleteAdmin":
                int adminId = Integer.parseInt(request.getParameter("adminId"));
                adao.deleteAdmin(adminId);
                break;
        }

        response.sendRedirect("Admin"); // Điều hướng lại đến trang danh sách
    }

    private void populateAdminFromRequest(HttpServletRequest request, Admin admin) {
        if (request.getParameter("adminId") != null) {
            admin.setId(Integer.parseInt(request.getParameter("adminId")));
        }
        admin.setUsername(request.getParameter("username"));
        admin.setPassword(request.getParameter("password"));
        admin.setName(request.getParameter("name"));
    }

    @Override
    public String getServletInfo() {
        return "Admin Controller";
    }
}
