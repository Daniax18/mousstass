package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.exception.DatabaseConnectionException;
import com.moustass.model.SignatureLog;
import com.moustass.view.SignatureView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link SignatureLog} persistence.
 * <p>
 * This class provides database access methods for storing and retrieving
 * digital signature records in order to ensure integrity, traceability,
 * and non-repudiation of signed files.
 * </p>
 */
public class SignatureLogRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    /**
     * Retrieves a signature log entry by its identifier.
     *
     * @param id the identifier of the signature log entry
     * @return the corresponding {@link SignatureLog}, or {@code null} if not found
     */
    public SignatureLog findById(int id) {
        String sql = "SELECT id, user_id, file_name, file_hash, signature_value, created_at FROM signature_logs WHERE id = ?";
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
     * Retrieves all signature log entries associated with a given user.
     *
     * @param userId the identifier of the user
     * @return a list of {@link SignatureLog} entries for the specified user
     */
    public List<SignatureLog> findAllByUserId(int userId) {
        String sql = "SELECT id, user_id, file_name, file_hash, signature_value, created_at FROM signature_logs WHERE user_id = ?";
        List<SignatureLog> list = new ArrayList<>();
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
     * Inserts a new signature log entry into the database.
     *
     * @param s the signature log entry to persist
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
    public boolean insert(SignatureLog s) {
        String sql = "INSERT INTO signature_logs (user_id, file_name, file_hash, signature_value) VALUES (?,?,?,?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getUserId());
            ps.setString(2, s.getFileName());
            ps.setString(3, s.getFileHash());
            ps.setString(4, s.getSignatureValue());
            int affected = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) s.setId(keys.getInt(1)); }
            return affected > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
    }

    /**
     * Maps a database result set row to a {@link SignatureLog} object.
     *
     * @param rs the {@link ResultSet} positioned at the current row
     * @return the mapped {@link SignatureLog} instance
     * @throws SQLException if a result set access error occurs
     */
    private SignatureLog mapRow(ResultSet rs) throws SQLException {
        SignatureLog s = new SignatureLog();
        s.setId(rs.getInt("id"));
        s.setUserId(rs.getInt("user_id"));
        s.setFileName(rs.getString("file_name"));
        s.setFileHash(rs.getString("file_hash"));
        s.setSignatureValue(rs.getString("signature_value"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setCreatedAt(ts.toLocalDateTime());
        return s;
    }

    /**
     * Maps a database result set row to a {@link SignatureView} object.
     * <p>
     * This method is typically used for read-only views
     * signature data with additional contextual information.
     * </p>
     *
     * @param rs the {@link ResultSet} positioned at the current row
     * @return the mapped {@link SignatureView} instance
     * @throws SQLException if a result set access error occurs
     */
    private SignatureView mapRowView(ResultSet rs) throws SQLException {
        SignatureView s = new SignatureView();
        s.setIdSignature(rs.getInt("id"));
        s.setFileName(rs.getString("file_name"));
        s.setUserName(rs.getString("username"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setDateSignature(ts.toLocalDateTime());
        return s;
    }

    /**
     * Retrieves all digital signature records using a view representation.
     *
     * @return a list of {@link SignatureView} objects representing all signatures
     */
    public List<SignatureView> findAllSignatures() {
        String sql = "select sl.id,sl.file_name, u.username, sl.created_at from signature_logs sl join users u where sl.user_id  = u.id";
        List<SignatureView> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowView(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
        return list;
    }
}
