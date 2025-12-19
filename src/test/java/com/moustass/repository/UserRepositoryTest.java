package com.moustass.repository;

import com.moustass.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

class UserRepositoryTest {


    @Test
    void mapRow_mapsAllFields()
            throws SQLException, NoSuchMethodException, SecurityException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        UserRepository repo = new UserRepository();
        ResultSet rs = Mockito.mock(ResultSet.class);

        Mockito.when(rs.getInt("id")).thenReturn(5);
        Mockito.when(rs.getString("firstname")).thenReturn("John");
        Mockito.when(rs.getString("lastname")).thenReturn("Doe");
        Mockito.when(rs.getString("username")).thenReturn("jdoe");
        Mockito.when(rs.getString("password_hash")).thenReturn("ph");
        Mockito.when(rs.getString("salt")).thenReturn("salt");
        Mockito.when(rs.getString("pk_public")).thenReturn("pk");
        Mockito.when(rs.getString("sk_private")).thenReturn("sk");
        Mockito.when(rs.getBoolean("must_change_pwd")).thenReturn(true);
        Mockito.when(rs.wasNull()).thenReturn(false);
        Mockito.when(rs.getBoolean("is_admin")).thenReturn(false);
        Mockito.when(rs.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        Method m = UserRepository.class.getDeclaredMethod("mapRow", ResultSet.class);
        m.setAccessible(true);
        User u = (User) m.invoke(repo, rs);

        Assertions.assertNotNull(u);
        Assertions.assertEquals(5, u.getId());
        Assertions.assertEquals("jdoe", u.getUsername());
        Assertions.assertEquals("ph", u.getPasswordHash());
    }
}
