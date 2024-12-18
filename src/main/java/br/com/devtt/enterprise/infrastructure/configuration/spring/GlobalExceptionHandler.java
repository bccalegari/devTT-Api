package br.com.devtt.enterprise.infrastructure.configuration.spring;

import br.com.devtt.enterprise.application.exceptions.CoreException;
import br.com.devtt.enterprise.infrastructure.adapters.dto.responses.OutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<OutputDto> handleGenericException(Exception exception) {
        log.error(exception.getMessage(), exception);
        var outputDto = new OutputDto(CoreException.DEFAULT_MESSAGE);
        return ResponseEntity.internalServerError().body(outputDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<OutputDto> handleAccessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        var outputDto = new OutputDto("Você não tem permissão para realizar essa ação!");
        return ResponseEntity.status(403).body(outputDto);
    }

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<OutputDto> handleCoreException(CoreException exception) {
        log.error(exception.getMessage(), exception);
        var outputDto = new OutputDto(exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(outputDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OutputDto> handleValidationExceptions(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        var errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        var outputDto = new OutputDto(errors.toString());
        return ResponseEntity.badRequest().body(outputDto);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<OutputDto> handleValidationExceptions(HandlerMethodValidationException exception) {
        log.error(exception.getMessage(), exception);
        var errors = exception.getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
        var outputDto = new OutputDto(errors.toString());
        return ResponseEntity.badRequest().body(outputDto);
    }
}