package com.github.bondarevv23.task_management_system.util;

import com.github.bondarevv23.task_management_system.exceptions.SecurityUtilsException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class SecurityUtils {
    private static final String USER_ID = "user_id";

    public static UUID getUserId() {
        return UUID.fromString(getClaim(USER_ID));
    }

    private static String getClaim(String key) {
        return Optional.ofNullable(getJwt().<String>getClaim(key))
                .orElseThrow(() -> new SecurityUtilsException(
                        String.format("jwt authentication has no claim with key '%s'", key)
                ));
    }

    private static Jwt getJwt() {
        Authentication authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(() -> new SecurityUtilsException("authentication is not established"));
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt;
        }
        throw new SecurityUtilsException("authentication is not a Jwt instance");
    }
}
