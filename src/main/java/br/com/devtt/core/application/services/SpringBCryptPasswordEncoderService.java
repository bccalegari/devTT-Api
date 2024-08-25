package br.com.devtt.core.application.services;

import br.com.devtt.core.abstractions.application.services.PasswordEncoderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Qualifier("SpringBCryptPasswordEncoderService")
public class SpringBCryptPasswordEncoderService implements PasswordEncoderService {
    private final PasswordEncoder encoder;

    @Override
    public String encode(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
           return encoder.matches(password, encodedPassword);
    }
}