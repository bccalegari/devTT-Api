package br.com.devtt.core.abstractions.application.services;

public interface PasswordEncoderService {
    String encode(String password);
    boolean matches(String password, String encodedPassword);
}