package com.gym.management.security;

import java.nio.charset.StandardCharsets;

/** Shared guard against BCrypt's 72-byte input limit. */
public final class PasswordPolicy {
    private PasswordPolicy() {}
    public static boolean exceedsBcryptLimit(String password) {
        return password.getBytes(StandardCharsets.UTF_8).length > 72;
    }
}
