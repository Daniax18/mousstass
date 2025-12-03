package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean insert(User u) {
        String sql = "INSERT INTO users (firstname, lastname, username, password_hash, salt, pk_public, sk_private, must_change_pwd, is_admin) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFirstname());
            ps.setString(2, u.getLastname());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPasswordHash());
            ps.setString(5, u.getSalt());
            ps.setString(6, u.getPkPublic());
            ps.setString(7, u.getSkPrivate());
            ps.setBoolean(8, u.getMustChangePwd() != null ? u.getMustChangePwd() : Boolean.TRUE);
            ps.setBoolean(9, u.getIsAdmin() != null ? u.getIsAdmin() : Boolean.FALSE);
            int affected = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getInt(1));
            }
            return affected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFirstname(rs.getString("firstname"));
        u.setLastname(rs.getString("lastname"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setSalt(rs.getString("salt"));
        u.setPkPublic(rs.getString("pk_public"));
        u.setSkPrivate(rs.getString("sk_private"));
        boolean must = rs.getBoolean("must_change_pwd");
        if (rs.wasNull()) u.setMustChangePwd(null); else u.setMustChangePwd(must);
        boolean admin = rs.getBoolean("is_admin");
        if (rs.wasNull()) u.setIsAdmin(null); else u.setIsAdmin(admin);
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }
}
