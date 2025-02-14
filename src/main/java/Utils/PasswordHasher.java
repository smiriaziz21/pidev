package Utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Hash a password using bcrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify a candidate password against a hashed password
    public static boolean checkPassword(String candidatePassword, String hashedPassword) {
        return BCrypt.checkpw(candidatePassword, hashedPassword);
    }
}