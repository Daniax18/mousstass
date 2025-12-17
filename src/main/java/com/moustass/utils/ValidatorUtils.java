package com.moustass.utils;

/**
 * Validation utility class.
 * <p>
 * This class provides static helper methods used to validate
 * user input according to defined security and business rules.
 * </p>
 * <p>
 * It is designed as a static utility class and cannot be instantiated.
 * </p>
 */
public final class ValidatorUtils {

    private ValidatorUtils(){}

    /**
     * Validates password strength rules.
     * <p>
     * This method enforces security constraints such as minimum length,
     * presence of uppercase and lowercase letters, digits, and special
     * characters.
     * </p>
     * <p>
     * If one or more rules are violated, an {@link IllegalArgumentException}
     * is thrown containing all validation error messages.
     * </p>
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if the password does not meet
     *                                  the required security rules
     */
    public static void validatePasswordRules(String password) {
        StringBuilder errors = new StringBuilder();
        if (password == null) {
            errors.append("Password required. ");
        } else {
            if (password.length() < 12) errors.append("Le mot de passe doit contenir au moins 12 caractères. ");
            boolean hasUpper = false;
            boolean hasLower = false;
            boolean hasDigit = false;
            boolean hasSpecial = false;

            for (char c : password.toCharArray()) {
                if (Character.isUpperCase(c)) hasUpper = true;
                else if (Character.isLowerCase(c)) hasLower = true;
                else if (Character.isDigit(c)) hasDigit = true;
                else hasSpecial = true;
            }

            if (!hasUpper) errors.append("Au moins une majuscule requise. ");
            if (!hasLower) errors.append("Au moins une minuscule requise. ");
            if (!hasDigit) errors.append("Au moins un chiffre requis. ");
            if (!hasSpecial) errors.append("Au moins un caractère spécial requis. ");
        }
        if (!errors.isEmpty()) throw new IllegalArgumentException(errors.toString().trim());
    }
}
