package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.exception.DatabaseConnectionException;
import com.moustass.model.AuthEvent;
import com.moustass.model.AuthLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link AuthLog} persistence.
 * <p>
 * This class provides database access methods for storing and retrieving
 * authentication log entries, enabling security auditing and monitoring
 * of authentication events.
 * </p>
 */

public class AuthLogRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    /**
     * Retrieves an authentication log entry by its identifier.
     *
     * @param id the identifier of the authentication log entry
     * @return the corresponding {@link AuthLog}, or {@code null} if not found
     */
    public AuthLog findById(int id) {
        String sql = "SELECT id, user_id, event, id_address, created_at FROM auth_logs WHERE id = ?";
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
     * Retrieves all authentication log entries.
     *
     * @return a list of all {@link AuthLog} records
     */
    public List<AuthLog> findAll() {
        String sql = "SELECT id, user_id, event, id_address, created_at FROM auth_logs";
        List<AuthLog> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
        return list;
    }

    /**
     * Inserts a new authentication log entry into the database.
     *
     * @param a the authentication log entry to persist
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
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
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
    }

    /**
     * Maps a database result set row to an {@link AuthLog} object.
     *
     * @param rs the {@link ResultSet} positioned at the current row
     * @return the mapped {@link AuthLog} instance
     * @throws SQLException if a result set access error occurs
     */
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
