package br.com.devtt.core.abstractions.application.services;

public interface PasswordEncoderService {
    public String encode(String password);
    public boolean matches(String password, String encodedPassword);
}