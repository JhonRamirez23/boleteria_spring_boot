package com.codesa.prueba.shared.exception;

import com.codesa.prueba.shared.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "No encontrado", ex.getMessage(), null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex) {
        log.warn("Regla de negocio violada: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de negocio", ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        log.warn("Error de validación: {}", detalles);
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación",
                "Los datos enviados no son válidos", detalles);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Credenciales incorrectas");
        return buildResponse(HttpStatus.UNAUTHORIZED, "No autorizado",
                "Email o contraseña incorrectos", null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Acceso denegado",
                "No tiene permisos para realizar esta acción", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno",
                "Ocurrió un error inesperado", null);
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String error,
                                                    String mensaje, List<String> detalles) {
        ApiError apiError = ApiError.builder()
                .status(status.value())
                .error(error)
                .mensaje(mensaje)
                .detalles(detalles)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(apiError);
    }
}
