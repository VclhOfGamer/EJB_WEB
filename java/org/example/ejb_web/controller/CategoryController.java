package org.example.ejb_web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.example.ejb_web.dao.CategoryDAO;
import org.example.ejb_web.model.Category;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/Category", "/addCategory", "/editCategory", "/deleteCategory"})
public class CategoryController extends HttpServlet {

    @EJB
    private CategoryDAO cdao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String userPath = request.getServletPath();
        RequestDispatcher dispatcher;

        switch (userPath) {
            case "/addCategory":
                dispatcher = request.getRequestDispatcher("/WEB-INF/Category/addCategory.jsp");
                break;
            case "/editCategory":
                String editIdStr = request.getParameter("id");
                if (editIdStr != null) {
                    int editId = Integer.parseInt(editIdStr);
                    Category category = cdao.findById(editId);
                    request.setAttribute("category", category);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/Category/editCategory.jsp");
                break;


            case "/deleteCategory":
                String delIdStr = request.getParameter("id");
                if (delIdStr != null) {
                    int delId = Integer.parseInt(delIdStr);
                    Category delCat = cdao.findById(delId);
                    request.setAttribute("category", delCat);
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/Category/deleteCategory.jsp");
                break;


            default:
                List<Category> categories = cdao.findAll();
                Map<Integer, Integer> productCounts = new HashMap<>();
                for (Category cat : categories) {
                    int count = cdao.countProductsByCategoryId(cat.getId());
                    productCounts.put(cat.getId(), count);
                }
                request.setAttribute("categoryList", categories);
                request.setAttribute("productCounts", productCounts);
                dispatcher = request.getRequestDispatcher("/WEB-INF/AllCategory.jsp");
                break;

        }

        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        Category category = new Category();

        switch (userPath) {
            case "/addCategory":
                category.setName(request.getParameter("catName"));
                cdao.insertCategory(category);
                response.sendRedirect("Category?code=addSuccess&type=Category&name=" + category.getName());
                break;

            case "/editCategory":
                category.setId(Integer.parseInt(request.getParameter("catId")));
                category.setName(request.getParameter("catName"));
                cdao.updateCategory(category);
                response.sendRedirect("Category?code=editSuccess&type=Category&id=" + category.getId());
                break;

            case "/deleteCategory":
                int catId = Integer.parseInt(request.getParameter("catId"));
                cdao.deleteCategory(catId);
                response.sendRedirect("Category?code=deleteSuccess&type=Category&id=" + catId);
                break;
        }
    }

    @Override
    public String getServletInfo() {
        return "Category Controller";
    }
}
