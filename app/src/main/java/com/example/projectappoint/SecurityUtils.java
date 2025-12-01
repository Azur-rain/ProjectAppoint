// SecurityUtils.java
package com.example.projectappoint;

// You would need to add a library dependency (e.g., jBCrypt)
// import org.mindrot.jbcrypt.BCrypt; 

public class SecurityUtils {

    /**
     * Hashes a plain text password using a secure, slow algorithm.
     * THIS IS A MOCK IMPLEMENTATION. USE A LIBRARY LIKE JBCRYPT IN PRODUCTION.
     */
    public static String hashPassword(String plainPassword) {
        // In a real app, you would use:
        // return BCrypt.hashpw(plainPassword, BCrypt.gensalt()); 

        // Mock implementation for demonstration:
        return "hashed_" + plainPassword.hashCode();
    }
}