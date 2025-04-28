package org.example.ejb_web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginRouter", urlPatterns = {"", "/login"})
public class HomeRedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nếu là truy cập root "/", redirect sang /login?type=admin
        if (request.getServletPath().equals("")) {
            response.sendRedirect(request.getContextPath() + "/login?type=admin");
            return;
        }

        // Trường hợp còn lại: xử lý /login
        String type = request.getParameter("type");
        if (type == null) type = "admin";

        request.setAttribute("type", type);

        String code = request.getParameter("code");
        if (code != null) request.setAttribute("code", code);

        request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
    }
}
