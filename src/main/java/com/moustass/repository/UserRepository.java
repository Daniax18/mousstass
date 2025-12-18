package com.moustass.repository;

import com.moustass.config.DatabaseConfig;
import com.moustass.exception.DatabaseConnectionException;
import com.moustass.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing {@link User} persistence.
 * <p>
 * This class provides database access operations related to user accounts,
 * including creation, retrieval, deletion, and credential updates.
 * </p>
 */
public class UserRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    /**
     * Retrieves a user by its identifier.
     *
     * @param id the identifier of the user
     * @return the corresponding {@link User}, or {@code null} if not found
     */
    public User findById(int id) {
        String sql = "SELECT id, firstname, lastname, username, password_hash, salt, pk_public, sk_private, must_change_pwd, is_admin, created_at FROM users WHERE id = ?";
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
     * Retrieves all users from the database.
     *
     * @return a list of all {@link User} records
     */
    public List<User> findAll() {
        String sql = "SELECT id, firstname, lastname, username, password_hash, salt, pk_public, sk_private, must_change_pwd, is_admin, created_at FROM users";
        List<User> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
        return list;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param u the user to persist
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
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
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
    }

    /**
     * Deletes a user by its identifier.
     *
     * @param id the identifier of the user to delete
     * @return {@code true} if the deletion was successful, {@code false} otherwise
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
    }

    /**
     * Retrieves a user by its username.
     *
     * @param username the unique username of the user
     * @return the corresponding {@link User}, or {@code null} if not found
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, firstname, lastname, username, password_hash, salt, pk_public, sk_private, must_change_pwd, is_admin, created_at FROM users WHERE username = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
        return null;
    }

    /**
     * Maps a database result set row to a {@link User} object.
     *
     * @param rs the {@link ResultSet} positioned at the current row
     * @return the mapped {@link User} instance
     * @throws SQLException if a result set access error occurs
     */
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

    /**
     * Updates the password credentials of a user.
     * <p>
     * This method is typically used during password change.
     * </p>
     *
     * @param u the user containing the updated password information
     * @return {@code true} if the update was successful, {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updatePassword(User u) throws SQLException{
        if (u == null || u.getId() == null) {
            throw new IllegalArgumentException("User and user id required");
        }
        String sql = "UPDATE users SET password_hash = ?,salt = ?,must_change_pwd = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getPasswordHash());
            ps.setString(2, u.getSalt());
            ps.setBoolean(3, u.getMustChangePwd());
            ps.setInt(4, u.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error db : " + e.getMessage());
        }
    }
}
