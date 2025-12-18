package com.moustass.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorUtilsTest {
    @Test
    void validatePasswordRules_validPassword_shouldNotThrowException() {
        assertDoesNotThrow(() ->
                ValidatorUtils.validatePasswordRules("StrongPwd!123")
        );
    }

    @Test
    void validatePasswordRules_nullPassword_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules(null)
        );

        assertTrue(exception.getMessage().contains("Password required"));
    }

    @Test
    void validatePasswordRules_tooShort_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules("Abc!123")
        );

        assertTrue(exception.getMessage().contains("12 caractères"));
    }

    @Test
    void validatePasswordRules_missingUppercase_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules("lowercase!123")
        );

        assertTrue(exception.getMessage().contains("majuscule"));
    }

    @Test
    void validatePasswordRules_missingLowercase_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules("UPPERCASE!123")
        );

        assertTrue(exception.getMessage().contains("minuscule"));
    }

    @Test
    void validatePasswordRules_missingDigit_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules("NoDigit!Password")
        );

        assertTrue(exception.getMessage().contains("chiffre"));
    }

    @Test
    void validatePasswordRules_missingSpecialCharacter_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules("NoSpecial12345")
        );

        assertTrue(exception.getMessage().contains("caractère spécial"));
    }

    @Test
    void validatePasswordRules_multipleViolations_shouldReturnAllErrors() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ValidatorUtils.validatePasswordRules("short")
        );

        assertAll(
                () -> assertTrue(exception.getMessage().contains("12 caractères")),
                () -> assertTrue(exception.getMessage().contains("majuscule")),
                () -> assertTrue(exception.getMessage().contains("chiffre")),
                () -> assertTrue(exception.getMessage().contains("caractère spécial"))
        );
    }
}