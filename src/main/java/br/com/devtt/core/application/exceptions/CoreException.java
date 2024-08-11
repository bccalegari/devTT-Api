package br.com.devtt.core.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoreException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5364721193702629378L;
    public static final String DEFAULT_MESSAGE = "Um erro inesperado ocorreu. Por favor, tente novamente mais tarde.";
    protected String message = DEFAULT_MESSAGE;
    protected Integer httpStatus = 500;
}