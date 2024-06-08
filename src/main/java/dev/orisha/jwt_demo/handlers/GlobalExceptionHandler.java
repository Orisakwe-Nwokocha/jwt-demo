package dev.orisha.jwt_demo.handlers;


import dev.orisha.jwt_demo.dto.responses.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("Bad request")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }
}
