package org.example.ejb_web.dao;

import org.example.ejb_web.model.Admin;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import org.example.ejb_web.model.Staff;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AdminDAO {

    @Resource(lookup = "jdbc/MySQLPool")
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<Admin> selectAllAdmin() {
        return filterAdmins(null, null);
    }

    public List<Admin> filterAdmins(String usernameKeyword, String nameKeyword) {
        List<Admin> admins = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM admin WHERE 1=1");

        if (usernameKeyword != null && !usernameKeyword.isEmpty()) {
            query.append(" AND username LIKE ?");
        }
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            query.append(" AND name LIKE ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (usernameKeyword != null && !usernameKeyword.isEmpty()) {
                ps.setString(index++, "%" + usernameKeyword + "%");
            }
            if (nameKeyword != null && !nameKeyword.isEmpty()) {
                ps.setString(index++, "%" + nameKeyword + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setName(rs.getString("name"));
                admins.add(admin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }

    public List<Admin> sortAdmins(String sortBy, boolean ascending) {
        List<Admin> admins = new ArrayList<>();
        String baseQuery = "SELECT * FROM admin";

        // Danh sách field hợp lệ
        List<String> validFields = List.of("id", "username", "name");
        if (!validFields.contains(sortBy)) {
            sortBy = "id"; // default
        }

        String direction = ascending ? "ASC" : "DESC";
        String query = baseQuery + " ORDER BY " + sortBy + " " + direction;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setName(rs.getString("name"));
                admins.add(admin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }


    public void insertAdmin(Admin admin) {
        String query = "INSERT INTO admin (username, password, name) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAdmin(Admin admin) {
        String query = "UPDATE admin SET username = ?, password = ?, name = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setString(3, admin.getName());
            ps.setInt(4, admin.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAdmin(int id) {
        String query = "DELETE FROM admin WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        boolean isLogin = false;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                isLogin = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isLogin;
    }

    public Admin findByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setName(rs.getString("name"));
                return admin;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Admin findById(int id) {
        String query = "SELECT * FROM admin WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setName(rs.getString("name"));
                return admin;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
