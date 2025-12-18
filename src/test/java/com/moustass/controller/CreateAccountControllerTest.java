package com.moustass.controller;

import com.moustass.model.User;
import com.moustass.service.CreateAccountService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Optional;

public class CreateAccountControllerTest {

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    public void onCreate_callsService_and_showsInfo() throws Exception {
        CreateAccountController ctrl = new CreateAccountController();

        TextField tfFirst = Mockito.mock(TextField.class);
        TextField tfLast = Mockito.mock(TextField.class);
        TextField tfUser = Mockito.mock(TextField.class);
        PasswordField pf = Mockito.mock(PasswordField.class);
        PasswordField pf2 = Mockito.mock(PasswordField.class);

        Mockito.when(tfFirst.getText()).thenReturn("John");
        Mockito.when(tfLast.getText()).thenReturn("Doe");
        Mockito.when(tfUser.getText()).thenReturn("jdoe");
        Mockito.when(pf.getText()).thenReturn("pass123");
        Mockito.when(pf2.getText()).thenReturn("pass123");

        setPrivateField(ctrl, "firstname", tfFirst);
        setPrivateField(ctrl, "lastname", tfLast);
        setPrivateField(ctrl, "username", tfUser);
        setPrivateField(ctrl, "password", pf);
        setPrivateField(ctrl, "confirmPassword", pf2);

        CreateAccountService mockSvc = Mockito.mock(CreateAccountService.class);
        User u = new User();
        u.setId(7);
        u.setUsername("jdoe");
        Mockito.when(mockSvc.createAccount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean())).thenReturn(u);

        setPrivateField(ctrl, "service", mockSvc);

        try (MockedConstruction<Alert> mocked = Mockito.mockConstruction(Alert.class, (mock, ctx) -> {
            Mockito.when(mock.showAndWait()).thenReturn(Optional.of(ButtonType.OK));
        })) {
            Assertions.assertDoesNotThrow(() -> ctrl.onCreate());
        }

        Mockito.verify(mockSvc).createAccount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean());
    }
}
