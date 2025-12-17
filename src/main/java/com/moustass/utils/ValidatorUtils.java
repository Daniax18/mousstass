package com.moustass.utils;

public final class ValidatorUtils {

    private ValidatorUtils(){}
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
