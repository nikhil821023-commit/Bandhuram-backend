package com.bandhuram.backend.service;

import com.bandhuram.backend.dto.LoginRequest;
import com.bandhuram.backend.dto.LoginResponse;
import com.bandhuram.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(req.username(), "ADMIN");
        return new LoginResponse(token, req.username(), "ADMIN");
    }
}