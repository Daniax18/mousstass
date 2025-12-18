package com.moustass.service;

import com.moustass.model.User;
import com.moustass.repository.ActivityLogRepository;
import com.moustass.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class CreateAccountServiceTest {

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    public void createAccount_success() throws Exception {
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

        User u = svc.createAccount("John","Doe","jdoe","pass123","pass123", null, false);

        Assertions.assertNotNull(u);
        Assertions.assertEquals(42, u.getId());
        Assertions.assertEquals("jdoe", u.getUsername());
        Mockito.verify(mockUserRepo).insert(Mockito.any());
    }

    @Test
    public void createAccount_usernameExists_throws() throws Exception {
        CreateAccountService svc = new CreateAccountService();
        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        ActivityLogRepository mockActRepo = Mockito.mock(ActivityLogRepository.class);

        Mockito.when(mockUserRepo.findByUsername("jdoe")).thenReturn(new User());
        setPrivateField(svc, "userRepository", mockUserRepo);
        setPrivateField(svc, "activityLogRepository", mockActRepo);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                svc.createAccount("John","Doe","jdoe","pass123","pass123", null, false)
        );
    }

    @Test
    public void createAccount_adminDoubleVerification_required() throws Exception {
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
                svc.createAccount("John","Doe","jdoe","pass123","pass123", 1, false)
        );
    }

    @Test
    public void createAccount_passwordMismatch_throws() throws Exception {
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
    public void createAccount_missingUsername_throws() throws Exception {
        CreateAccountService svc = new CreateAccountService();
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                svc.createAccount("John","Doe",null,"pass1","pass1", null, false)
        );
    }
}
