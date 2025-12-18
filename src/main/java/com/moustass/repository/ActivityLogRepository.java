package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.exception.DatabaseConnectionException;
import com.moustass.model.ActivityLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link ActivityLog} persistence.
 * <p>
 * This class provides database access methods for retrieving and storing
 * activity log records related to user actions.
 * </p>
 */

public class ActivityLogRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    /**
     * Retrieves an activity log entry by its identifier.
     *
     * @param id the identifier of the activity log
     * @return the corresponding {@link ActivityLog}, or {@code null} if not found
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Retrieves all activity log entries associated with a given user.
     *
     * @param userId the identifier of the user
     * @return a list of {@link ActivityLog} entries for the specified user
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Inserts a new activity log entry into the database.
     *
     * @param a the activity log to persist
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Maps a database result set row to an {@link ActivityLog} object.
     *
     * @param rs the {@link ResultSet} positioned at the current row
     * @return the mapped {@link ActivityLog} instance
     * @throws SQLException if a result set access error occurs
     */
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
