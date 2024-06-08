package dev.orisha.jwt_demo.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    @JsonProperty("userId")
    private Long id;
    private String username;
    private String message;
    private String token;
}
