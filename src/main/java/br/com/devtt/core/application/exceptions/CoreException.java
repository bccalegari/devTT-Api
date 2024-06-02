package br.com.devtt.core.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

@Getter
@AllArgsConstructor
public class CoreException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5364721193702629378L;
    public static final String DEFAULT_MESSAGE = "An error occurred while processing the request, please try again later.";
    protected String message = DEFAULT_MESSAGE;
    protected Integer httpStatus = 500;
}