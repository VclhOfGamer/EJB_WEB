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

@WebServlet(name = "Product", urlPatterns = {"/Product", "/addProduct", "/editProduct", "/deleteProduct"})
public class ProductController extends HttpServlet {

    @EJB
    private ProductDAO productDAO;

    @EJB
    private CategoryDAO categoryDAO;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();

        switch (userPath) {
            case "/addProduct":
                List<Category> categories = categoryDAO.findAll();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/WEB-INF/Product/addProduct.jsp").forward(request, response);
                break;

            case "/editProduct":
                int editId = Integer.parseInt(request.getParameter("id"));
                Product editProduct = productDAO.findById(editId);
                List<Category> editCategories = categoryDAO.findAll();
                request.setAttribute("product", editProduct);
                request.setAttribute("categories", editCategories);
                request.getRequestDispatcher("/WEB-INF/Product/editProduct.jsp").forward(request, response);
                break;

            case "/deleteProduct":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                Product deleteProduct = productDAO.findById(deleteId);

                if (deleteProduct != null && deleteProduct.getCategoryId() > 0) {
                    Category category = categoryDAO.findById(deleteProduct.getCategoryId());
                    if (category != null) {
                        deleteProduct.setCategoryName(category.getName());
                    } else {
                        deleteProduct.setCategoryName("Unknown Category");
                    }
                }

                request.setAttribute("product", deleteProduct);
                request.getRequestDispatcher("/WEB-INF/Product/deleteProduct.jsp").forward(request, response);
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
                request.getRequestDispatcher("/WEB-INF/AllProduct.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        Product product = new Product();
        populateProductFromRequest(request, product);

        switch (userPath) {
            case "/addProduct":
                product.setDateAdded(LocalDateTime.now());
                productDAO.insert(product);
                response.sendRedirect("Product?code=addSuccess&type=Product");
                return;

            case "/editProduct":
                productDAO.update(product);
                response.sendRedirect("Product?code=editSuccess&type=Product");
                return;

            case "/deleteProduct":
                // Make sure to handle the ID properly when deleting
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    int delId = Integer.parseInt(idParam);
                    productDAO.delete(delId);
                    response.sendRedirect("Product?code=deleteSuccess&type=Product");
                } else {
                    // Handle error case - redirect back with error parameter
                    response.sendRedirect("Product?code=deleteError&type=Product");
                }
                return;
        }
    }

    private void populateProductFromRequest(HttpServletRequest request, Product product) {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            product.setId(Integer.parseInt(idParam));
        }

        product.setName(request.getParameter("name"));

        String quantityParam = request.getParameter("quantity");
        if (quantityParam != null && !quantityParam.isEmpty()) {
            product.setQuantity(Integer.parseInt(quantityParam));
        }

        String priceParam = request.getParameter("price");
        if (priceParam != null && !priceParam.isEmpty()) {
            product.setPrice(Double.parseDouble(priceParam));
        }

        product.setUnit(request.getParameter("unit"));

        String addedByParam = request.getParameter("addedBy");
        if (addedByParam != null && !addedByParam.isEmpty()) {
            product.setAddedBy(Integer.parseInt(addedByParam));
        }

        String categoryIdParam = request.getParameter("categoryId");
        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            product.setCategoryId(Integer.parseInt(categoryIdParam));
        }

        if (product.getDateAdded() == null) {
            product.setDateAdded(LocalDateTime.now());
        }
    }
}