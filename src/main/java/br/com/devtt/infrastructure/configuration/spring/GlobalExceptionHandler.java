package br.com.devtt.infrastructure.configuration.spring;

import br.com.devtt.core.application.exceptions.CoreException;
import br.com.devtt.infrastructure.adapters.dto.responses.OutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OutputDto> handleGenericException(Exception exception) {
        log.error(exception.getMessage(), exception);
        OutputDto outputDto = new OutputDto(CoreException.DEFAULT_MESSAGE);
        return ResponseEntity.internalServerError().body(outputDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<OutputDto> handleAccessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        OutputDto outputDto = new OutputDto("Você não tem permissão para acessar este recurso!");
        return ResponseEntity.status(403).body(outputDto);
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<OutputDto> handleCoreException(CoreException exception) {
        log.error(exception.getMessage(), exception);
        OutputDto outputDto = new OutputDto(exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(outputDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OutputDto> handleValidationExceptions(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        OutputDto outputDto = new OutputDto(errors.toString());
        return ResponseEntity.badRequest().body(outputDto);
    }
}