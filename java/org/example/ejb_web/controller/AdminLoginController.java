package org.example.ejb_web.controller;

import org.example.ejb_web.dao.AdminDAO;
import org.example.ejb_web.model.Admin;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminLogin", urlPatterns = {"/AdminLogin", "/loginAdmin", "/logoutAdmin"})
public class AdminLoginController extends HttpServlet {

    @EJB
    private AdminDAO adminDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/AdminLogin":
                // Gửi tham số type để Login.jsp nhận biết là admin
                request.setAttribute("type", "admin");
                request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
                break;

            case "/logoutAdmin":
                HttpSession session = request.getSession(false);
                if (session != null) session.invalidate();
                response.sendRedirect("AdminLogin");
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/loginAdmin".equals(path)) {
            String username = request.getParameter("adminUsername");
            String password = request.getParameter("adminPass");

            boolean loginSuccess = adminDAO.login(username, password);

            if (loginSuccess) {
                Admin admin = adminDAO.findByUsername(username);

                if (admin != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("adminid", admin.getId());
                    session.setAttribute("adminname", admin.getName());

                    // ✅ Redirect đến servlet /admin/dashboard để load dữ liệu
                    response.sendRedirect(request.getContextPath() + "/AdminDashboard");
                } else {
                    request.setAttribute("code", "failLogin");
                    request.setAttribute("type", "admin");
                    request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
                }


            } else {
                request.setAttribute("code", "failLogin");
                request.setAttribute("type", "admin");
                request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public String getServletInfo() {
        return "Admin Login Controller";
    }
}
