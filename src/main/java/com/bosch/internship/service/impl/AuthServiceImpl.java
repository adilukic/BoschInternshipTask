package com.bosch.internship.service.impl;

import com.bosch.internship.controller.dto.LoginRequest;
import com.bosch.internship.controller.dto.RegisterRequest;
import com.bosch.internship.entity.User;
import com.bosch.internship.repo.UserRepository;
import com.bosch.internship.service.AuthService;
import com.bosch.internship.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }

        User newUser = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .name(registerRequest.getName())
                .lastName(registerRequest.getLastName())
                .phoneNum(registerRequest.getPhoneNum())
                .role("USER")
                .build();
        userRepository.save(newUser);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        try {
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole())
                    .build();

            return jwtUtil.generateAccessToken(userDetails);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Token generation failed");
        }
    }
}
