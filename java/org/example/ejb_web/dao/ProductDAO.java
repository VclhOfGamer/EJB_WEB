package org.example.ejb_web.dao;

import org.example.ejb_web.model.Product;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProductDAO {

    @Resource(lookup = "jdbc/MySQLPool")
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Map ResultSet to Product
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setQuantity(rs.getInt("quantity"));
        p.setPrice(rs.getDouble("price"));
        p.setUnit(rs.getString("unit"));
        p.setDateAdded(rs.getTimestamp("date_added").toLocalDateTime());
        p.setAddedBy(rs.getInt("added_by"));
        p.setCategoryId(rs.getInt("category_id"));
        if (hasColumn(rs, "staff_name")) {
            p.setStaffName(rs.getString("staff_name"));
        }
        if (hasColumn(rs, "category_name")) {
            p.setCategoryName(rs.getString("category_name"));
        }
        return p;
    }

    // Map Product to PreparedStatement
    private void mapProductToPreparedStatement(Product product, PreparedStatement ps, boolean includeIdAtEnd) throws SQLException {
        ps.setString(1, product.getName());
        ps.setInt(2, product.getQuantity());
        ps.setDouble(3, product.getPrice());
        ps.setString(4, product.getUnit());
        ps.setTimestamp(5, Timestamp.valueOf(product.getDateAdded()));
        ps.setInt(6, product.getAddedBy());
        ps.setInt(7, product.getCategoryId());

        if (includeIdAtEnd) {
            ps.setInt(8, product.getId());
        }
    }

    public void insert(Product product) {
        String query = "INSERT INTO product (name, quantity, price, unit, date_added, added_by, category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            mapProductToPreparedStatement(product, ps, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(Product product) {
        String query = "UPDATE product SET name = ?, quantity = ?, price = ?, unit = ?, date_added = ?, added_by = ?, category_id = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            mapProductToPreparedStatement(product, ps, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(int id) {
        String query = "DELETE FROM product WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        String query = """
    SELECT p.*, s.name AS staff_name, c.name AS category_name
    FROM product p
    JOIN staff s ON p.added_by = s.id
    JOIN category c ON p.category_id = c.id
""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Product findById(int id) {
        Product product = null;
        String query = "SELECT * FROM product WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }


    public List<Product> searchByName(String nameKeyword) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name LIKE ? COLLATE utf8mb4_general_ci";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + nameKeyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public int getTotalInventory() {
        String sql = "SELECT SUM(quantity * price) FROM product";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Pour mettre à jour la quantité d'un produit
    public void updateQuantity(int productId, int newQuantity) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Product SET quantity = ? WHERE id = ?")) {

            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductInfo(int productId, java.util.Date dateAdded, String addedBy) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Product SET date_added = ?, added_by = ? WHERE id = ?")) {

            java.sql.Date sqlDate = new java.sql.Date(dateAdded.getTime());
            stmt.setDate(1, sqlDate);
            stmt.setString(2, addedBy);
            stmt.setInt(3, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> findByCateGory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String query = """
                SELECT p.*, s.name AS staff_name, c.name AS category_name
                FROM product p
                JOIN staff s ON p.added_by = s.id
                JOIN category c ON p.category_id = c.id
                WHERE p.category_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Product> search(String name, Double price, Integer categoryId) {
        List<Product> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT p.*, s.name AS staff_name, c.name AS category_name FROM product p JOIN staff s ON p.added_by = s.id JOIN category c ON p.category_id = c.id WHERE 1=1");

        if (name != null && !name.isEmpty()) {
            query.append(" AND p.name LIKE ?");
        }
        if (price != null) {
            query.append(" AND p.price <= ?");
        }
        if (categoryId != null) {
            query.append(" AND p.category_id = ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (name != null && !name.isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }
            if (price != null) {
                ps.setDouble(index++, price);
            }
            if (categoryId != null) {
                ps.setInt(index++, categoryId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}


