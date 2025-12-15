package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.model.SignatureLog;
import com.moustass.view.SignatureView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignatureLogRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public SignatureLog findById(int id) {
        String sql = "SELECT * FROM signature_logs WHERE id = ?";
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

    public List<SignatureLog> findAllByUserId(int userId) {
        String sql = "SELECT * FROM signature_logs WHERE user_id = ?";
        List<SignatureLog> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

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
            throw new RuntimeException(e);
        }
    }

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

    private SignatureView mapRowView(ResultSet rs) throws SQLException {
        SignatureView s = new SignatureView();
        s.setIdSignature(rs.getInt("id"));
        s.setFileName(rs.getString("file_name"));
        s.setUserName(rs.getString("username"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setDateSignature(ts.toLocalDateTime());
        return s;
    }

    public List<SignatureView> findAllSignatures() {
        String sql = "select sl.id,sl.file_name, u.username, sl.created_at from signature_logs sl join users u where sl.user_id  = u.id";
        List<SignatureView> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowView(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
