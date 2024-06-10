package thesis.utils.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHelper {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());

        StringBuilder hexString = new StringBuilder(2 * hashedPassword.length);
        for (byte b : hashedPassword) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean checkPassword(String originalPassword, String storedPasswordHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(originalPassword.getBytes());

        StringBuilder hexString = new StringBuilder(2 * hashedPassword.length);
        for (int i = 0; i < hashedPassword.length; i++) {
            String hex = Integer.toHexString(0xff & hashedPassword[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().equals(storedPasswordHash);
    }
}
