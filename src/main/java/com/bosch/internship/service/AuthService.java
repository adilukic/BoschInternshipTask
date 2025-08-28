package com.bosch.internship.service;

import com.bosch.internship.controller.dto.LoginRequest;
import com.bosch.internship.controller.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerRequestn);
    String login(LoginRequest loginRequest);
}
