package dev.orisha.jwt_demo.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ss a")
    private LocalDateTime requestTime;
    private boolean success;
    private String error;
    private String message;
    private String path;
}
