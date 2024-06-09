package dev.orisha.jwt_demo.services;

import dev.orisha.jwt_demo.data.models.User;
import dev.orisha.jwt_demo.data.repositories.UserRepository;
import dev.orisha.jwt_demo.dto.requests.LoginRequest;
import dev.orisha.jwt_demo.dto.responses.LoginResponse;
import dev.orisha.jwt_demo.dto.requests.RegisterRequest;
import dev.orisha.jwt_demo.dto.responses.RegisterResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final JwtEncoder jwtEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;


    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String token = generateToken(user);
        User savedUser = userRepository.save(user);
        var response = modelMapper.map(savedUser, RegisterResponse.class);
        response.setMessage("User registered successfully");
        response.setToken(token);
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            UserDetails user = (UserDetails) authentication.getPrincipal();
            System.out.println("user object: "+user);

            String token = tokenService.generateToken(authentication);
            var response = modelMapper.map(user, LoginResponse.class);
            response.setMessage("Login successful");
            response.setToken(token);
            return response;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }


 /*       User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        var response = modelMapper.map(user, LoginResponse.class);
        response.setToken(generateToken(user));
        response.setMessage("Login successful");
        return response;*/
    }


    private String generateToken(User user) {
        Instant now = now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRoles())
                .issuedAt(now)
                .expiresAt(now.plus(1, HOURS))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
