package br.com.devtt.core.application.exceptions;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class JwtSecretNotFoundException extends CoreException {
    @Serial private static final long serialVersionUID = 9069964177833946801L;
}