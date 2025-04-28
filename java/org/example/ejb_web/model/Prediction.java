package org.example.ejb_web.model;

public class Prediction {
    private String productId;
    private String productName;
    private String category;
    private int quantityPredicted;

    // Getters + Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantityPredicted() { return quantityPredicted; }
    public void setQuantityPredicted(int quantityPredicted) { this.quantityPredicted = quantityPredicted; }
}
