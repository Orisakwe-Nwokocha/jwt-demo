package dev.orisha.jwt_demo.services;

import dev.orisha.jwt_demo.data.models.User;
import dev.orisha.jwt_demo.data.repositories.UserRepository;
import dev.orisha.jwt_demo.dto.requests.LoginRequest;
import dev.orisha.jwt_demo.dto.responses.LoginResponse;
import dev.orisha.jwt_demo.dto.requests.RegisterRequest;
import dev.orisha.jwt_demo.dto.responses.RegisterResponse;
import dev.orisha.jwt_demo.services.security.TokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            throw new RuntimeException("Username already exists");
        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        RegisterResponse response = modelMapper.map(savedUser, RegisterResponse.class);
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            UserDetails user = (UserDetails) authentication.getPrincipal();
            System.out.println("user object: " + user);

            LoginResponse response = modelMapper.map(user, LoginResponse.class);
            response.setMessage("Login successful");
            String token = tokenService.generateToken(authentication);
            response.setToken(token);
            return response;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

    }

}
