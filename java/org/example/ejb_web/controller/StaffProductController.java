package org.example.ejb_web.controller;

import org.example.ejb_web.dao.CategoryDAO;
import org.example.ejb_web.dao.ProductDAO;
import org.example.ejb_web.model.Category;
import org.example.ejb_web.model.Product;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "StaffProduct", urlPatterns = {"/StaffProduct", "/StaffAddProduct", "/StaffEditProduct", "/StaffDeleteProduct"})
public class StaffProductController extends HttpServlet {

    @EJB
    private ProductDAO productDAO;

    @EJB
    private CategoryDAO categoryDAO;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect("login");
            return;
        }

        Integer staffId = (Integer) session.getAttribute("id");
        String staffName = (String) session.getAttribute("staffName");
        request.setAttribute("staffId", staffId);
        request.setAttribute("staffName", staffName);
        request.setAttribute("isStaff", true); // Flag to indicate this is a staff user

        String userPath = request.getServletPath();

        switch (userPath) {
            case "/StaffAddProduct":
                List<Category> categories = categoryDAO.findAll();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/Product/addProduct.jsp").forward(request, response);
                break;

            case "/StaffEditProduct":
                try {
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Product editProduct = productDAO.findById(editId);
                    if (editProduct == null) {
                        response.sendRedirect("StaffProduct?error=productnotfound");
                        return;
                    }
                    List<Category> editCategories = categoryDAO.findAll();
                    request.setAttribute("product", editProduct);
                    request.setAttribute("categories", editCategories);
                    request.getRequestDispatcher("/WEB-INF/Product/editProduct.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("StaffProduct?error=invalidid");
                }
                break;

            case "/StaffDeleteProduct":
                try {
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    Product deleteProduct = productDAO.findById(deleteId);
                    if (deleteProduct == null) {
                        response.sendRedirect("StaffProduct?error=productnotfound");
                        return;
                    }
                    request.setAttribute("product", deleteProduct);
                    request.getRequestDispatcher("/WEB-INF/Product/deleteProduct.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.sendRedirect("StaffProduct?error=invalidid");
                }
                break;

            default:
                List<Product> products;
                String name = request.getParameter("search");
                if (name != null && !name.trim().isEmpty()) {
                    products = productDAO.searchByName(name.trim());
                } else {
                    products = productDAO.findAll();
                }
                request.setAttribute("products", products);
                request.getRequestDispatcher("/WEB-INF/StaffView/StaffAllProduct.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if staff is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect("login");
            return;
        }

        String userPath = request.getServletPath();

        // Get staff ID from session
        Integer staffId = (Integer) session.getAttribute("id");

        switch (userPath) {
            case "/StaffAddProduct":
                try {
                    Product addProduct = new Product();
                    populateProductFromRequest(request, addProduct, staffId);
                    addProduct.setDateAdded(LocalDateTime.now());
                    productDAO.insert(addProduct);
                    response.sendRedirect("StaffProduct?code=addSuccess&type=Product");
                } catch (Exception e) {
                    response.sendRedirect("StaffAddProduct?error=" + e.getMessage());
                }
                return;

            case "/StaffEditProduct":
                try {
                    Product editProduct = new Product();
                    populateProductFromRequest(request, editProduct, staffId);
                    productDAO.update(editProduct);
                    response.sendRedirect("StaffProduct?code=editSuccess&type=Product");
                } catch (Exception e) {
                    response.sendRedirect("StaffEditProduct?error=" + e.getMessage() + "&id=" + request.getParameter("id"));
                }
                return;

            case "/StaffDeleteProduct":
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    try {
                        int delId = Integer.parseInt(idParam);
                        productDAO.delete(delId);
                        response.sendRedirect("StaffProduct?code=deleteSuccess&type=Product");
                    } catch (NumberFormatException e) {
                        response.sendRedirect("StaffProduct?error=invalidid");
                    } catch (Exception e) {
                        response.sendRedirect("StaffProduct?error=deletefailed");
                    }
                } else {
                    response.sendRedirect("StaffProduct?error=noidprovided");
                }
                return;
        }
    }

    private void populateProductFromRequest(HttpServletRequest request, Product product, Integer staffId) {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            product.setId(Integer.parseInt(idParam));
        }

        product.setName(request.getParameter("name"));
        product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        product.setPrice(Double.parseDouble(request.getParameter("price")));
        product.setUnit(request.getParameter("unit"));

        // For staff users, always use the staffId from the session
        product.setAddedBy(staffId);

        product.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));

        if (product.getDateAdded() == null) {
            product.setDateAdded(LocalDateTime.now());
        }
    }
}