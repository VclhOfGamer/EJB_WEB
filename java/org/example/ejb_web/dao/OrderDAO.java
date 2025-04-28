package org.example.ejb_web.dao;

import org.example.ejb_web.model.Order;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrderDAO {

    @Resource(lookup = "jdbc/MySQLPool")
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setOrderType(rs.getString("order_type"));
        o.setStatus(rs.getString("status"));
        o.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        o.setStaffId(rs.getInt("staff_id"));
        return o;
    }

    public int insert(Order order) {
        int generatedId = 0;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO Orders (order_type, order_date, status, staff_id) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getOrderType());

            // Convertir LocalDateTime en Timestamp
            if (order.getOrderDate() != null) {
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(order.getOrderDate()));
            } else {
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }

            stmt.setString(3, order.getStatus());
            stmt.setInt(4, order.getStaffId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public Order findById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double totalPaidOrders() {
        String sql = """
        SELECT SUM(od.quantity * p.price) AS total
        FROM orders o
        JOIN orders_product od ON o.id = od.order_id
        JOIN product p ON od.product_id = p.id
        WHERE o.status = 'PAID'
    """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double totalUnPaidOrders() {
        String sql = """
        SELECT SUM(od.quantity * p.price) AS total
        FROM orders o
        JOIN orders_product od ON o.id = od.order_id
        JOIN product p ON od.product_id = p.id
        WHERE o.status = 'UNPAID'
    """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    public int countAllOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getOrderCountByDateRange(String startDate, String endDate, String orderType, String status) {
        String sql = """
        SELECT COUNT(*) 
        FROM orders 
        WHERE order_type = ? AND status = ? AND order_date BETWEEN ? AND ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderType);
            ps.setString(2, status);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void update(Order order) {
        String sql = """
            UPDATE orders
            SET order_type = ?, order_date = ?, status = ?, staff_id = ?
            WHERE id = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getOrderType());
            ps.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            ps.setString(3, order.getStatus());
            ps.setInt(4, order.getStaffId());
            ps.setInt(5, order.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
                String sql = "DELETE FROM orders WHERE id = ?";
                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }

