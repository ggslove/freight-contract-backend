package com.freight.contract.controller;

import com.freight.contract.entity.Role;
import com.freight.contract.entity.User;
import com.freight.contract.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Basic ")) {
                response.put("error", "Missing or invalid authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String base64Credentials = authorization.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);

            if (parts.length != 2) {
                response.put("error", "Invalid credentials format");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String username = parts[0];
            String password = parts[1];

            User user = userService.getUserByUsername(username)
                    .orElse(null);

            if (user == null) {
                response.put("error", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.put("error", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("username", user.getUsername());
            successResponse.put("email", user.getEmail());
            successResponse.put("roles", user.getRoles().stream().map(Role::getCode).collect(Collectors.toList()));
            successResponse.put("status", user.getStatus().name());
            successResponse.put("realName", user.getRealName());

            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            response.put("error", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}