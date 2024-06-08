package dev.orisha.jwt_demo.services;

import dev.orisha.jwt_demo.dto.requests.LoginRequest;
import dev.orisha.jwt_demo.dto.responses.LoginResponse;
import dev.orisha.jwt_demo.dto.requests.RegisterRequest;
import dev.orisha.jwt_demo.dto.responses.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);


}
