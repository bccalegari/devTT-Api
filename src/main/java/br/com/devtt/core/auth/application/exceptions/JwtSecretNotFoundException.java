package br.com.devtt.core.auth.application.exceptions;

import br.com.devtt.enterprise.application.exceptions.CoreException;
import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class JwtSecretNotFoundException extends CoreException {
    @Serial private static final long serialVersionUID = 9069964177833946801L;
}