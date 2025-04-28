package org.example.ejb_web.dao;

import org.example.ejb_web.model.Staff;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class StaffDAO {

    @Resource(lookup = "jdbc/MySQLPool")
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Map ResultSet to Staff
    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setUsername(rs.getString("username"));
        s.setPassword(rs.getString("password"));
        s.setAddress(rs.getString("address"));
        return s;
    }

    // Map Staff to PreparedStatement
    private void mapStaffToPreparedStatement(Staff staff, PreparedStatement ps, boolean includeIdAtEnd) throws SQLException {
        ps.setString(1, staff.getName());
        ps.setString(2, staff.getUsername());
        ps.setString(3, staff.getPassword());
        ps.setString(4, staff.getAddress());
        if (includeIdAtEnd) {
            ps.setInt(5, staff.getId());
        }
    }

    public void insert(Staff staff) {
        String sql = "INSERT INTO staff (name, username, password, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            mapStaffToPreparedStatement(staff, ps, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Staff> findAll() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public void update(Staff staff) {
        String sql = "UPDATE staff SET name = ?, username = ?, password = ?, address = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            mapStaffToPreparedStatement(staff, ps, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM staff WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";
        boolean isValid = false;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                isValid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public Staff findByUsername(String username) {
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                staff = mapResultSetToStaff(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staff;
    }
    public Staff findById(int id) {
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                staff = mapResultSetToStaff(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staff;
    }

    // Filter staff
    public List<Staff> filterStaff(String name, String address) {
        List<Staff> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM staff WHERE 1=1");

        if (name != null && !name.isEmpty()) sql.append(" AND name LIKE ?");
        if (address != null && !address.isEmpty()) sql.append(" AND address LIKE ?");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (name != null && !name.isEmpty()) ps.setString(index++, "%" + name + "%");
            if (address != null && !address.isEmpty()) ps.setString(index++, "%" + address + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Sort staff
    public List<Staff> sortStaff(String sortBy, boolean ascending) {
        List<Staff> list = new ArrayList<>();
        List<String> validSorts = List.of("name", "username", "id", "address");
        String sql = "SELECT * FROM staff";

        if (sortBy != null && validSorts.contains(sortBy)) {
            sql += " ORDER BY " + sortBy + (ascending ? " ASC" : " DESC");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
