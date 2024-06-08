package dev.orisha.jwt_demo.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterResponse {
    @JsonProperty("userId")
    private Long id;
    private String username;
    private Set<String> roles;
    private String message;
    private String token;
}
