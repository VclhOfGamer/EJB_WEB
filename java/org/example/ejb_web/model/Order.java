package org.example.ejb_web.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private String orderType;
    private String status;
    private LocalDateTime orderDate;
    private int staffId;

    public Order() {}

    public Order(int id, String orderType, String status, LocalDateTime orderDate, int staffId) {
        this.id = id;
        this.orderType = orderType;
        this.status = status;
        this.orderDate = orderDate;
        this.staffId = staffId;
    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
