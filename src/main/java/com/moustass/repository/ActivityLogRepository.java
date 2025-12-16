package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.exception.DatabaseConnectionException;
import com.moustass.model.ActivityLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public ActivityLog findById(int id) throws SQLException {
        String sql = "SELECT id, user_id, action, details, created_at FROM activity_logs WHERE id = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
        return null;
    }

    public List<ActivityLog> findAllByUserId(int userId) throws SQLException {
        String sql = "SELECT id, user_id, action, details, created_at FROM activity_logs WHERE user_id = ?";
        List<ActivityLog> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
        return list;
    }

    public boolean insert(ActivityLog a) throws SQLException {
        String sql = "INSERT INTO activity_logs (user_id, action, details) VALUES (?,?,?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (a.getUserId() != null) ps.setInt(1, a.getUserId()); else ps.setNull(1, Types.INTEGER);
            ps.setString(2, a.getAction().name());
            ps.setString(3, a.getDetails());
            int affected = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) a.setId(keys.getInt(1)); }
            return affected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
    }

    private ActivityLog mapRow(ResultSet rs) throws SQLException {
        ActivityLog a = new ActivityLog();
        a.setId(rs.getInt("id"));
        int uid = rs.getInt("user_id"); if (rs.wasNull()) a.setUserId(null); else a.setUserId(uid);
        a.setAction(ActivityLog.TypeAction.valueOf(rs.getString("action")));
        a.setDetails(rs.getString("details"));
        Timestamp ts = rs.getTimestamp("created_at"); if (ts != null) a.setCreatedAt(ts.toLocalDateTime());
        return a;
    }
}
