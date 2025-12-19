package com.moustass.service;

import com.moustass.model.User;
import com.moustass.repository.ActivityLogRepository;
import com.moustass.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

class CreateAccountServiceTest {

    private static void setPrivateField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    void createAccount_success()
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, SQLException, NoSuchAlgorithmException {
        CreateAccountService svc = new CreateAccountService();

        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        ActivityLogRepository mockActRepo = Mockito.mock(ActivityLogRepository.class);

        Mockito.when(mockUserRepo.findByUsername("jdoe")).thenReturn(null);
        Mockito.when(mockUserRepo.insert(Mockito.any())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(42);
            return true;
        });
        Mockito.when(mockActRepo.insert(Mockito.any())).thenReturn(true);

        setPrivateField(svc, "userRepository", mockUserRepo);
        setPrivateField(svc, "activityLogRepository", mockActRepo);

        User u = svc.createAccount("John","Doe","jdoe","StrongPwd!123","StrongPwd!123", null, false);

        Assertions.assertNotNull(u);
        Assertions.assertEquals(42, u.getId());
        Assertions.assertEquals("jdoe", u.getUsername());
        Mockito.verify(mockUserRepo).insert(Mockito.any());
    }

    @Test
    void createAccount_usernameExists_throws() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        CreateAccountService svc = new CreateAccountService();
        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        ActivityLogRepository mockActRepo = Mockito.mock(ActivityLogRepository.class);

        Mockito.when(mockUserRepo.findByUsername("jdoe")).thenReturn(new User());
        setPrivateField(svc, "userRepository", mockUserRepo);
        setPrivateField(svc, "activityLogRepository", mockActRepo);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                svc.createAccount("John","Doe","jdoe","StrongPwd!123","StrongPwd!123", null, false)
        );
    }

    @Test
    void createAccount_adminDoubleVerification_required() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        CreateAccountService svc = new CreateAccountService();
        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        ActivityLogRepository mockActRepo = Mockito.mock(ActivityLogRepository.class);

        Mockito.when(mockUserRepo.findByUsername("jdoe")).thenReturn(null);
        User admin = new User();
        admin.setIsAdmin(true);
        Mockito.when(mockUserRepo.findById(1)).thenReturn(admin);

        setPrivateField(svc, "userRepository", mockUserRepo);
        setPrivateField(svc, "activityLogRepository", mockActRepo);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                svc.createAccount("John","Doe","jdoe","StrongPwd!123","StrongPwd!123", 1, false)
        );
    }

    @Test
    void createAccount_passwordMismatch_throws() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        CreateAccountService svc = new CreateAccountService();
        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        ActivityLogRepository mockActRepo = Mockito.mock(ActivityLogRepository.class);

        Mockito.when(mockUserRepo.findByUsername("jdoe")).thenReturn(null);
        setPrivateField(svc, "userRepository", mockUserRepo);
        setPrivateField(svc, "activityLogRepository", mockActRepo);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                svc.createAccount("John","Doe","jdoe","pass1","pass2", null, false)
        );
    }

    @Test
    void createAccount_missingUsername_throws() throws IllegalArgumentException {
        CreateAccountService svc = new CreateAccountService();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                svc.createAccount("John","Doe",null,"StrongPwd!123","StrongPwd!123", null, false)
        );
    }
}
