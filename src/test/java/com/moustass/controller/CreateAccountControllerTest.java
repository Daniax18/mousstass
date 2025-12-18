package com.moustass.controller;

import com.moustass.model.User;
import com.moustass.service.CreateAccountService;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    public static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) { }
    }

    @Test
    public void onCreate_callsService_and_showsInfo() throws Exception {
        CreateAccountController ctrl = new CreateAccountController();

        // utilisez de vrais contrôles JavaFX au lieu de mocks
        TextField tfFirst = new TextField();
        TextField tfLast = new TextField();
        TextField tfUser = new TextField();
        PasswordField pf = new PasswordField();
        PasswordField pf2 = new PasswordField();

        tfFirst.setText("John");
        tfLast.setText("Doe");
        tfUser.setText("jdoe");
        pf.setText("StrongPwd!123");
        pf2.setText("StrongPwd!123");

        setPrivateField(ctrl, "firstname", tfFirst);
        setPrivateField(ctrl, "lastname", tfLast);
        setPrivateField(ctrl, "username", tfUser);
        setPrivateField(ctrl, "password", pf);
        setPrivateField(ctrl, "confirmPassword", pf2);

        User u = new User();
        u.setId(7);
        u.setUsername("jdoe");

        // injecter un mock de service au lieu d'attendre une construction
        CreateAccountService mockSvc = Mockito.mock(CreateAccountService.class);
        Mockito.when(mockSvc.createAccount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
               .thenReturn(u);
        setPrivateField(ctrl, "service", mockSvc);

        try (MockedConstruction<Alert> mocked = Mockito.mockConstruction(Alert.class, (mock, ctx) -> {
            Mockito.when(mock.showAndWait()).thenReturn(Optional.of(ButtonType.OK));
        })) {
            Assertions.assertDoesNotThrow(() -> ctrl.onCreate());
        }

        // vérifier que le mock a été appelé
        Mockito.verify(mockSvc).createAccount(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean());
    }
}
