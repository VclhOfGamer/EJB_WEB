package org.example.ejb_web.dao;
import org.example.ejb_web.model.OrdersProduct;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class OrdersProductDAO {

    @Resource(lookup = "jdbc/MySQLPool")
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void insert(OrdersProduct op) {
        String query = "INSERT INTO orders_product (order_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, op.getOrderId());
            ps.setInt(2, op.getProductId());
            ps.setInt(3, op.getQuantity());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(int orderId) {
        String query = "DELETE FROM orders_product WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(OrdersProduct op) {
        String query = "UPDATE orders_product SET order_id = ?, product_id = ?, quantity = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, op.getOrderId());
            ps.setInt(2, op.getProductId());
            ps.setInt(3, op.getQuantity());
            ps.setInt(4, op.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OrdersProduct> findByOrderId(int orderId) {
        List<OrdersProduct> list = new ArrayList<>();
        String query = "SELECT * FROM orders_product WHERE order_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrdersProduct op = new OrdersProduct();
                op.setId(rs.getInt("id"));
                op.setOrderId(rs.getInt("order_id"));
                op.setProductId(rs.getInt("product_id"));
                op.setQuantity(rs.getInt("quantity"));
                list.add(op);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void updateQuantity(int orderId, int productId, int quantity) {
        String query = "UPDATE orders_product SET quantity = ? WHERE order_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, orderId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean existsInOrder(int orderId, int productId) {
        String query = "SELECT COUNT(*) FROM orders_product WHERE order_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count > 0, the product exists in the order
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteProductFromOrder(int orderId, int productId) {
        String query = "DELETE FROM orders_product WHERE order_id = ? AND product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Return true if any rows were deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalOrdersByDateAndTypeStatus(String date, String type, String status) {
        double total = 0;
        String sql = """
        SELECT SUM(op.quantity * p.price) AS total
        FROM orders o
        JOIN orders_product op ON o.id = op.order_id
        JOIN product p ON op.product_id = p.id
        WHERE DATE(o.order_date) = ? AND o.order_type = ? AND o.status = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, date);
            ps.setString(2, type);
            ps.setString(3, status);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }


    public OrdersProduct findById(int id) {
        String query = "SELECT * FROM orders_product WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrdersProduct op = new OrdersProduct();
                op.setId(rs.getInt("id"));
                op.setOrderId(rs.getInt("order_id"));
                op.setProductId(rs.getInt("product_id"));
                op.setQuantity(rs.getInt("quantity"));
                return op;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public double getTotalOrdersByTypeAndStatus(String type, String status) {
        String query = """
        SELECT SUM(op.quantity * p.price) AS total 
        FROM orders_product op
        JOIN product p ON op.product_id = p.id
        JOIN orders o ON op.order_id = o.id
        WHERE o.status = ? AND o.order_type = ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setString(2, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public OrdersProduct findByOrderIdAndProductId(int orderId, int productId) {
        String query = "SELECT * FROM orders_product WHERE order_id = ? AND product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrdersProduct op = new OrdersProduct();
                op.setId(rs.getInt("id"));
                op.setOrderId(rs.getInt("order_id"));
                op.setProductId(rs.getInt("product_id"));
                op.setQuantity(rs.getInt("quantity"));
                return op;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public double getTotalOrdersByDateRangeAndTypeStatus(String startDate, String endDate, String type, String status) {
        String sql = """
            SELECT SUM(op.quantity * p.price) AS total
            FROM orders o
            JOIN orders_product op ON o.id = op.order_id
            JOIN product p ON op.product_id = p.id
            WHERE o.order_date BETWEEN ? AND ? AND o.order_type = ? AND o.status = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setString(3, type);
            ps.setString(4, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public List<OrdersProduct> findAll() {
        List<OrdersProduct> list = new ArrayList<>();
        String query = "SELECT * FROM orders_product";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrdersProduct op = new OrdersProduct();
                op.setId(rs.getInt("id"));
                op.setOrderId(rs.getInt("order_id"));
                op.setProductId(rs.getInt("product_id"));
                op.setQuantity(rs.getInt("quantity"));
                list.add(op);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<OrdersProduct> findByProductId(int productId) {
        List<OrdersProduct> list = new ArrayList<>();
        String query = "SELECT * FROM orders_product WHERE product_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrdersProduct op = new OrdersProduct();
                op.setId(rs.getInt("id"));
                op.setOrderId(rs.getInt("order_id"));
                op.setProductId(rs.getInt("product_id"));
                op.setQuantity(rs.getInt("quantity"));
                list.add(op);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}



