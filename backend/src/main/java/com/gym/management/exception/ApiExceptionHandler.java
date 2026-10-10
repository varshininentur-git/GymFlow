package com.gym.management.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,Object> notFound(RuntimeException e) { return error(404, e.getMessage()); }
    @ExceptionHandler(ConflictException.class) @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,Object> conflict(RuntimeException e) { return error(409, e.getMessage()); }
    @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> badRequest(RuntimeException e) { return error(400, e.getMessage()); }
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> validation(MethodArgumentNotValidException e) { return error(400, e.getBindingResult().getFieldErrors().stream().map(f -> f.getField() + ": " + f.getDefaultMessage()).findFirst().orElse("Invalid request")); }
    private Map<String,Object> error(int status, String message) { return Map.of("timestamp", Instant.now(), "status", status, "message", message); }
}
