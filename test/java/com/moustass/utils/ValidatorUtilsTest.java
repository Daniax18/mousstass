package com.moustass.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidatorUtilsTest {

    @Test
    public void validatePasswordRules_validPassword_passes() {
        String good = "Abcdef1!ghij"; // 12 chars, upper, lower, digit, special
        Assertions.assertDoesNotThrow(() -> ValidatorUtils.validatePasswordRules(good));
    }

    @Test
    public void validatePasswordRules_null_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validatePasswordRules(null));
    }

    @Test
    public void validatePasswordRules_missingRequirements_throws() {
        String bad = "short";
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> ValidatorUtils.validatePasswordRules(bad));
        Assertions.assertTrue(ex.getMessage().contains("Le mot de passe"));
    }
}
