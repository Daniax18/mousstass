package com.moustass.controller;


import java.lang.reflect.Field;
import java.util.Optional;

public class CreateAccountControllerTest {

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
