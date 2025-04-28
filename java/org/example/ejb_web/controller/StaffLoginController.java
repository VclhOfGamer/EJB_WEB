package org.example.ejb_web.controller;

import org.example.ejb_web.dao.StaffDAO;
import org.example.ejb_web.model.Staff;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "StaffLogin", urlPatterns = {"/StaffLogin", "/loginStaff", "/logoutStaff"})
public class StaffLoginController extends HttpServlet {

    @EJB
    private StaffDAO staffDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/StaffLogin":
                // Gửi tham số type để Login.jsp biết là staff
                request.setAttribute("type", "staff");
                request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
                break;

            case "/logoutStaff":
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect("StaffLogin");
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

        if ("/loginStaff".equals(path)) {
            try {
                String username = request.getParameter("staffUsername");
                String password = request.getParameter("staffPass");

                System.out.println("username: " + username);

                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    request.setAttribute("code", "failLogin");
                    request.setAttribute("type", "staff");
                    request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
                    return;
                }

                boolean loginSuccess = staffDAO.login(username, password);
                System.out.println("Résultat login: " + loginSuccess);

                if (loginSuccess) {
                    Staff staff = staffDAO.findByUsername(username);

                    if (staff != null) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("id", staff.getId());
                        session.setAttribute("name", staff.getName());
                        session.setAttribute("staffName", staff.getName());

                        System.out.println("Session  " + staff.getName() + ", ID: " + session.getId());

                        // Change this line in StaffLoginController.doPost()
                        response.sendRedirect(request.getContextPath() + "/StaffDashboard");
                        return;
                    }
                }

                request.setAttribute("code", "failLogin");
                request.setAttribute("type", "staff");
                request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("code", "failLogin");
                request.setAttribute("type", "staff");
                request.getRequestDispatcher("/WEB-INF/Login.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public String getServletInfo() {
        return "Controller Staff Login";
    }
}