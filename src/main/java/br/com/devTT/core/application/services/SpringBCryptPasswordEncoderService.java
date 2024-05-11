package br.com.devTT.core.application.services;

import br.com.devTT.core.abstractions.application.services.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SpringBCryptPasswordEncoderService")
public class SpringBCryptPasswordEncoderService implements PasswordEncoderService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
           return encoder.matches(password, encodedPassword);
    }
}

