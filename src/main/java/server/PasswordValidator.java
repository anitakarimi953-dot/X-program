package server;

public class PasswordValidator {

    public static boolean isValid(String password) {

        if (password == null) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {

            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }

            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }

            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit;
    }
}