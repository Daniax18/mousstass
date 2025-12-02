package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.model.UploadedFile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UploadedFileRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public UploadedFile findById(int id) {
        String sql = "SELECT * FROM files WHERE id = ?";
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

    public List<UploadedFile> findAllByUserId(int userId) {
        String sql = "SELECT * FROM files WHERE user_id = ?";
        List<UploadedFile> list = new ArrayList<>();
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

    public boolean insert(UploadedFile f) {
        String sql = "INSERT INTO files (user_id, file_name, file_hash) VALUES (?,?,?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, f.getUserId());
            ps.setString(2, f.getFileName());
            ps.setString(3, f.getFileHash());
            int affected = ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) f.setId(keys.getInt(1)); }
            return affected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UploadedFile mapRow(ResultSet rs) throws SQLException {
        UploadedFile f = new UploadedFile();
        f.setId(rs.getInt("id"));
        f.setUserId(rs.getInt("user_id"));
        f.setFileName(rs.getString("file_name"));
        f.setFileHash(rs.getString("file_hash"));
        Timestamp ts = rs.getTimestamp("uploaded_at"); if (ts != null) f.setUploadedAt(ts.toLocalDateTime());
        return f;
    }
}
