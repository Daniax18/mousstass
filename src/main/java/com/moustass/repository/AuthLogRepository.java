package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.model.AuthEvent;
import com.moustass.model.AuthLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthLogRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public AuthLog findById(int id) {
        String sql = "SELECT * FROM auth_logs WHERE id = ?";
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

    public List<AuthLog> findAll() {
        String sql = "SELECT * FROM auth_logs";
        List<AuthLog> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public boolean insert(AuthLog a) {
        String sql = "INSERT INTO auth_logs (user_id, event, ip_address) VALUES (?,?,?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (a.getUserId() != null) ps.setInt(1, a.getUserId()); else ps.setNull(1, Types.INTEGER);
            ps.setString(2, a.getEvent() != null ? a.getEvent().name() : null);
            ps.setString(3, a.getIpAddress());
            int affected = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) a.setId(keys.getInt(1)); }
            return affected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthLog mapRow(ResultSet rs) throws SQLException {
        AuthLog a = new AuthLog();
        a.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id"); if (rs.wasNull()) a.setUserId(null); else a.setUserId(uid);
        String ev = rs.getString("event"); if (ev != null) a.setEvent(AuthEvent.valueOf(ev));
        a.setIpAddress(rs.getString("ip_address"));
        Timestamp ts = rs.getTimestamp("created_at"); if (ts != null) a.setCreatedAt(ts.toLocalDateTime());
        return a;
    }
}
