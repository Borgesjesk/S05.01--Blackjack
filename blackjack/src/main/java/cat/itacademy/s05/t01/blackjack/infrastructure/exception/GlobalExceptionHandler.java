package cat.itacademy.s05.t01.blackjack.infrastructure.exception;

import cat.itacademy.s05.t01.blackjack.application.exception.GameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleGameNotFoundException(GameNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStateTransition(IllegalStateException ex, HttpServletRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();


        errorAttributes.put("timestamp", Instant.now().toString());
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", message);

        return new ResponseEntity<>(errorAttributes, status);
    }
}