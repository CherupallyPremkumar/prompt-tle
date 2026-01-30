package com.handmade.tle.auth.configuration.controller;

import com.handmade.tle.auth.security.jwt.JwtTokenProvider;
import com.handmade.tle.shared.dto.*;
import com.handmade.tle.shared.model.User;
import com.handmade.tle.shared.model.Role;
import com.handmade.tle.shared.model.Acl;
import com.handmade.tle.shared.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Auth Service Controller
 * Matches the spec for login, registration, and token management.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());
        User user = userRepository.findByEmail(request.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials", "AUTH_001"));
        }

        List<String> authorities = getAuthorities(user);
        String token = tokenProvider.generateAccessToken(user.getId(), authorities);

        return ResponseEntity.ok(ApiResponse.success(AuthResponse.builder()
                .accessToken(token)
                .username(user.getUsername())
                .roles(authorities)
                .build()));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Email already exists", "AUTH_002"));
        }
        return ResponseEntity.ok(ApiResponse.success("User registered successfully. Please login."));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Map<String, String>>> refreshToken(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies() == null ? new Cookie[0] : request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid refresh token", "AUTH_003"));
        }

        String userId = tokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findByIdWithRoles(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("User not found", "AUTH_004"));
        }

        List<String> authorities = getAuthorities(user);
        String newAccessToken = tokenProvider.generateAccessToken(userId, authorities);
        return ResponseEntity.ok(ApiResponse.success(Map.of("accessToken", newAccessToken)));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestParam String token) {
        boolean isValid = tokenProvider.validateToken(token);
        return ResponseEntity.ok(ApiResponse.success(isValid));
    }

    private List<String> getAuthorities(User user) {
        Set<String> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(role.getName().name());
            for (Acl acl : role.getAcls()) {
                authorities.add(acl.getName());
            }
        }
        return new ArrayList<>(authorities);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser(HttpServletRequest request) {
        String token = com.handmade.tle.auth.security.util.CookieUtils.getCookie(request, "access_token")
                .map(Cookie::getValue)
                .orElse(null);

        if (token == null || !tokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthenticated", "AUTH_005"));
        }

        String userId = tokenProvider.getUserIdFromToken(token);
        User user = userRepository.findByIdWithRoles(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("User not found", "AUTH_004"));
        }

        List<String> authorities = getAuthorities(user);

        return ResponseEntity.ok(ApiResponse.success(AuthResponse.builder()
                .accessToken(null) // Do not return token in body, it's in the cookie
                .username(user.getUsername())
                .roles(authorities)
                .build()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logout request received");
        com.handmade.tle.auth.security.util.CookieUtils.deleteCookie(request, response, "access_token");
        com.handmade.tle.auth.security.util.CookieUtils.deleteCookie(request, response, "refresh_token");
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
}
