package com.eventmanagement.config;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordEncoder {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128; // 128 bits = 16 bytes
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String encode(CharSequence rawPassword) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = hash(rawPassword.toString().toCharArray(), salt);
        return String.format("%s:%s", 
            Base64.getEncoder().encodeToString(salt),
            Base64.getEncoder().encodeToString(hash));
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || !encodedPassword.contains(":")) {
            return false;
        }
        String[] parts = encodedPassword.split(":", 2);
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
        byte[] actualHash = hash(rawPassword.toString().toCharArray(), salt);
        return slowEquals(expectedHash, actualHash);
    }

    private byte[] hash(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}