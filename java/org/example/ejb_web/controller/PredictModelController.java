package org.example.ejb_web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.ejb_web.model.Prediction;

import java.io.*;
import java.nio.file.*;
import java.util.*;


@WebServlet("/PredictModel")
public class PredictModelController extends HttpServlet {

    private static final String FILE_NAME = "/WEB-INF/data/prediction_next_month.csv";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Prediction> predictions = readFilteredPredictions(request);
        request.setAttribute("predictions", predictions);
        request.getRequestDispatcher("/WEB-INF/PredictModel.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String productId = request.getParameter("productId");
        String newQuantityStr = request.getParameter("quantity");

        String filePath = getServletContext().getRealPath(FILE_NAME);
        List<String> updatedLines = new ArrayList<>();
        boolean isFirstLine = true;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line);  // giữ header
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                String id = parts[0].trim();
                String subCategory = parts[3].trim();

                if (subCategory.equalsIgnoreCase("Bookcases")) continue;

                if (id.equals(productId)) {
                    if ("delete".equalsIgnoreCase(action)) {
                        continue; // bỏ dòng này khỏi danh sách
                    } else if ("edit".equalsIgnoreCase(action)) {
                        // Cập nhật quantity mới
                        try {
                            int newQuantity = Integer.parseInt(newQuantityStr.trim());
                            parts[4] = String.valueOf(newQuantity);
                        } catch (NumberFormatException e) {
                            parts[4] = "0";
                        }
                        line = String.join(",", parts);
                    }
                }

                updatedLines.add(line);
            }
        }

        // Ghi lại file CSV
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath))) {
            for (String line : updatedLines) {
                bw.write(line);
                bw.newLine();
            }
        }

        // Hiển thị lại
        List<Prediction> predictions = readFilteredPredictions(request);
        request.setAttribute("predictions", predictions);
        request.getRequestDispatcher("/WEB-INF/PredictModel.jsp").forward(request, response);
    }

    private List<Prediction> readFilteredPredictions(HttpServletRequest request) throws IOException {
        String filePath = getServletContext().getRealPath(FILE_NAME);
        List<Prediction> predictions = new ArrayList<>();
        boolean isFirstLine = true;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                String subCategory = parts[3].trim();
                if (subCategory.equalsIgnoreCase("Bookcases")) continue;

                Prediction p = new Prediction();
                p.setProductId(parts[0].trim());
                p.setProductName(parts[1].trim());
                p.setCategory(parts[2].trim());

                try {
                    p.setQuantityPredicted(Integer.parseInt(parts[4].trim()));
                } catch (NumberFormatException e) {
                    p.setQuantityPredicted(0);
                }

                predictions.add(p);
            }
        }

        return predictions;
    }
}
